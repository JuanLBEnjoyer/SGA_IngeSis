package co.edu.uniquindio.proyecto.infraestructure.persistence;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.exception.ExcepcionDeSolicitudNoEncontrada;
import co.edu.uniquindio.proyecto.domain.repository.SolicitudRepository;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.*;
import co.edu.uniquindio.proyecto.infrastructure.persistence.jpa.GeneradorCodigoJpa;
import co.edu.uniquindio.proyecto.infrastructure.persistence.jpa.SolicitudJpaRepository;
import co.edu.uniquindio.proyecto.infrastructure.persistence.jpa.UsuarioJpaRepository;
import co.edu.uniquindio.proyecto.infrastructure.persistence.mapper.SolicitudPersistenceMapperImpl;
import co.edu.uniquindio.proyecto.infrastructure.persistence.mapper.UsuarioPersistenceMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de integración para SolicitudJpaRepository.
 *
 */
@DataJpaTest(properties = "spring.sql.init.mode=never")
@Import({
        SolicitudJpaRepository.class,
        UsuarioJpaRepository.class,
        GeneradorCodigoJpa.class,
        SolicitudPersistenceMapperImpl.class,
        UsuarioPersistenceMapperImpl.class
})
class SolicitudJpaRepositoryTest {

    @Autowired
    private SolicitudRepository solicitudRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private GeneradorCodigoJpa generadorCodigo;

    private Usuario solicitante;
    private Usuario responsable;

    @BeforeEach
    void setUp() {
        solicitante = new Usuario(
                new Documento("123456", TipoDeDocumento.CEDULA),
                "Juan Perez",
                new Email("juan.perez@uqvirtual.edu.co"),
                RolUsuario.ESTUDIANTE);

        responsable = new Usuario(
                new Documento("789456", TipoDeDocumento.CEDULA),
                "Ana Gomez",
                new Email("ana.gomez@uniquindio.edu.co"),
                RolUsuario.ADMINISTRATIVO);

        // Persistimos los usuarios para que el mapper pueda reconstruirlos al
        // cargar solicitudes. guardar() hace upsert por documento → idempotente.
        usuarioRepository.guardar(solicitante);
        usuarioRepository.guardar(responsable);
    }

    private Solicitud crearSolicitudValida(String codigo) {
        return new Solicitud(
                new CodigoSolicitud(codigo),
                "Necesito registrar Programación Avanzada para este semestre",
                solicitante,
                TipoDeSolicitud.REGISTRAR_ASIGNATURA);
    }

    // ── Guardar y recuperar ───────────────────────────────────────────────────

    @Test
    void debeGuardarYRecuperarPorCodigo() {
        solicitudRepository.guardar(crearSolicitudValida("001"));

        Solicitud recuperada = solicitudRepository.obtenerPorCodigo(new CodigoSolicitud("001"));

        assertNotNull(recuperada);
        assertEquals("001", recuperada.getCodigo().valor());
        assertEquals(EstadoDeSolicitud.REGISTRADA, recuperada.getEstado());
        assertEquals(TipoDeSolicitud.REGISTRAR_ASIGNATURA, recuperada.getTipo());
        assertEquals("Juan Perez", recuperada.getSolicitante().getNombre());
    }

    @Test
    void debePersistirHistorialInicialAlCrear() {
        solicitudRepository.guardar(crearSolicitudValida("001"));

        Solicitud recuperada = solicitudRepository.obtenerPorCodigo(new CodigoSolicitud("001"));

        assertEquals(1, recuperada.getHistorial().size());
        assertEquals(EstadoDeSolicitud.REGISTRADA, recuperada.getHistorial().get(0).estadoAsociado());
    }

    @Test
    void debePersistirHistorialCompleto() {
        Solicitud solicitud = crearSolicitudValida("001");
        solicitud.clasificar(PrioridadDeSolicitud.ALTO, "Inicio de semestre urgente");
        solicitud.asignarResponsable(responsable);
        solicitudRepository.guardar(solicitud);

        Solicitud recuperada = solicitudRepository.obtenerPorCodigo(new CodigoSolicitud("001"));

        assertEquals(3, recuperada.getHistorial().size());
        assertEquals(EstadoDeSolicitud.REGISTRADA, recuperada.getHistorial().get(0).estadoAsociado());
        assertEquals(EstadoDeSolicitud.CLASIFICADA, recuperada.getHistorial().get(1).estadoAsociado());
        assertEquals(EstadoDeSolicitud.EN_ATENCION, recuperada.getHistorial().get(2).estadoAsociado());
    }

    @Test
    void debeActualizarEstadoAlGuardarDosVeces() {
        Solicitud solicitud = crearSolicitudValida("001");
        solicitudRepository.guardar(solicitud);

        solicitud.clasificar(PrioridadDeSolicitud.MEDIO, "Justificación de prueba");
        solicitudRepository.guardar(solicitud);

        Solicitud recuperada = solicitudRepository.obtenerPorCodigo(new CodigoSolicitud("001"));
        assertEquals(EstadoDeSolicitud.CLASIFICADA, recuperada.getEstado());
        assertEquals(PrioridadDeSolicitud.MEDIO, recuperada.getPrioridad());
    }

    @Test
    void debeRecuperarResponsableAsignado() {
        Solicitud solicitud = crearSolicitudValida("001");
        solicitud.clasificar(PrioridadDeSolicitud.ALTO, "Urgente");
        solicitud.asignarResponsable(responsable);
        solicitudRepository.guardar(solicitud);

        Solicitud recuperada = solicitudRepository.obtenerPorCodigo(new CodigoSolicitud("001"));

        assertNotNull(recuperada.getResponsable());
        assertEquals("Ana Gomez", recuperada.getResponsable().getNombre());
    }

    // ── Consultas por estado paginado ─────────────────────────────────────────

    @Test
    void debeListarTodasPaginadas() {
        solicitudRepository.guardar(crearSolicitudValida("001"));
        solicitudRepository.guardar(crearSolicitudValida("002"));
        solicitudRepository.guardar(crearSolicitudValida("003"));

        PageRequest pageable = PageRequest.of(0, 10);
        Page<Solicitud> pagina = solicitudRepository.obtenerPorEstado(null, pageable);

        assertEquals(3, pagina.getTotalElements());
        assertEquals(1, pagina.getTotalPages());
    }

    @Test
    void debeListarPorEstadoPaginado() {
        Solicitud s1 = crearSolicitudValida("001");
        Solicitud s2 = crearSolicitudValida("002");
        Solicitud s3 = crearSolicitudValida("003");
        s2.clasificar(PrioridadDeSolicitud.ALTO, "Urgente");
        s3.clasificar(PrioridadDeSolicitud.BAJO, "Sin prisa");

        solicitudRepository.guardar(s1);
        solicitudRepository.guardar(s2);
        solicitudRepository.guardar(s3);

        PageRequest pageable = PageRequest.of(0, 10);
        Page<Solicitud> registradas = solicitudRepository.obtenerPorEstado(EstadoDeSolicitud.REGISTRADA, pageable);
        Page<Solicitud> clasificadas = solicitudRepository.obtenerPorEstado(EstadoDeSolicitud.CLASIFICADA, pageable);

        assertEquals(1, registradas.getTotalElements());
        assertEquals(2, clasificadas.getTotalElements());
    }

    @Test
    void debePaginarCorrectamenteCuandoHayMasElementosDeLosQueCapeLaPagina() {
        for (int i = 1; i <= 5; i++) {
            solicitudRepository.guardar(crearSolicitudValida(String.format("%03d", i)));
        }

        PageRequest primeraPagina = PageRequest.of(0, 2, Sort.by("codigo"));
        PageRequest segundaPagina = PageRequest.of(1, 2, Sort.by("codigo"));
        PageRequest terceraPagina = PageRequest.of(2, 2, Sort.by("codigo"));

        Page<Solicitud> p1 = solicitudRepository.obtenerPorEstado(null, primeraPagina);
        Page<Solicitud> p2 = solicitudRepository.obtenerPorEstado(null, segundaPagina);
        Page<Solicitud> p3 = solicitudRepository.obtenerPorEstado(null, terceraPagina);

        assertEquals(5, p1.getTotalElements());
        assertEquals(3, p1.getTotalPages());
        assertEquals(2, p1.getContent().size());
        assertEquals(2, p2.getContent().size());
        assertEquals(1, p3.getContent().size());
    }

    @Test
    void debeDevolverPaginaVaciaCuandoNoHaySolicitudesConEseEstado() {
        solicitudRepository.guardar(crearSolicitudValida("001"));

        PageRequest pageable = PageRequest.of(0, 10);
        Page<Solicitud> cerradas = solicitudRepository.obtenerPorEstado(EstadoDeSolicitud.CERRADA, pageable);

        assertEquals(0, cerradas.getTotalElements());
        assertTrue(cerradas.getContent().isEmpty());
    }

    // ── Casos de error ────────────────────────────────────────────────────────

    @Test
    void debeLanzarExcepcionAlBuscarCodigoInexistente() {
        assertThrows(ExcepcionDeSolicitudNoEncontrada.class,
                () -> solicitudRepository.obtenerPorCodigo(new CodigoSolicitud("999")));
    }

    /**
     * Verifica que guardar() con el mismo código dos veces hace UPDATE (upsert),
     * no INSERT duplicado. El segundo guardar conserva el estado más reciente.
     */
    @Test
    void debeRechazarCodigoDuplicado() {
        solicitudRepository.guardar(crearSolicitudValida("001"));
        // Guardar de nuevo la misma solicitud debe hacer UPDATE, no INSERT duplicado
        assertDoesNotThrow(() -> solicitudRepository.guardar(crearSolicitudValida("001")));

        // Verificar que sigue habiendo exactamente 1 solicitud con ese código
        Solicitud recuperada = solicitudRepository.obtenerPorCodigo(new CodigoSolicitud("001"));
        assertEquals("001", recuperada.getCodigo().valor());
    }

    // ── GeneradorCodigoJpa ────────────────────────────────────────────────────

    @Test
    void debeGenerarPrimerCodigoCuandoBDEstaVacia() {
        CodigoSolicitud codigo = generadorCodigo.generar();

        assertEquals("001", codigo.valor());
    }

    @Test
    void debeGenerarCodigoContinuandoDesdeMaxExistente() {
        solicitudRepository.guardar(crearSolicitudValida("001"));
        solicitudRepository.guardar(crearSolicitudValida("002"));

        CodigoSolicitud siguiente = generadorCodigo.generar();

        assertEquals("003", siguiente.valor());
    }
}
