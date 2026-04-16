package co.edu.uniquindio.proyecto.application.usecase;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.exception.ExcepcionDeSolicitudNoEncontrada;
import co.edu.uniquindio.proyecto.domain.exception.ExcepcionDeReglaDeDominio;
import co.edu.uniquindio.proyecto.domain.repository.SolicitudRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CerrarSolicitudUseCaseTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    @InjectMocks
    private CerrarSolicitudUseCase useCase;

    // ── Fixture helpers ───────────────────────────────────────────────────────

    private Solicitud crearSolicitudAtendida() {
        Usuario solicitante = new Usuario(
                new Documento("123456", TipoDeDocumento.CEDULA),
                "Juan Perez",
                new Email("juan.perez@uqvirtual.edu.co"),
                RolUsuario.ESTUDIANTE);

        Usuario responsable = new Usuario(
                new Documento("789456", TipoDeDocumento.CEDULA),
                "Ana Gomez",
                new Email("ana.gomez@uqvirtual.edu.co"),
                RolUsuario.ADMINISTRATIVO);

        Solicitud solicitud = new Solicitud(
                new CodigoSolicitud("001"),
                "Registro de materias primer semestre",
                solicitante,
                TipoDeSolicitud.REGISTRAR_ASIGNATURA);

        solicitud.clasificar(PrioridadDeSolicitud.ALTO, "Inicio de semestre");
        solicitud.asignarResponsable(responsable);
        solicitud.atender();
        return solicitud;
    }

    private Solicitud crearSolicitudRegistrada() {
        Usuario solicitante = new Usuario(
                new Documento("123456", TipoDeDocumento.CEDULA),
                "Juan Perez",
                new Email("juan.perez@uqvirtual.edu.co"),
                RolUsuario.ESTUDIANTE);

        return new Solicitud(
                new CodigoSolicitud("002"),
                "Consulta sobre pensum académico",
                solicitante,
                TipoDeSolicitud.CONSULTA_ACADEMICA);
    }

    // ── Tests ─────────────────────────────────────────────────────────────────

    @Test
    void debeCerrarSolicitudCuandoEstaAtendida() {
        // Arrange
        Solicitud solicitud = crearSolicitudAtendida();
        when(solicitudRepository.obtenerPorCodigo(any(CodigoSolicitud.class)))
                .thenReturn(solicitud);

        // Act
        useCase.ejecutar("001", "Solicitud resuelta satisfactoriamente");

        // Assert
        assertEquals(EstadoDeSolicitud.CERRADA, solicitud.getEstado());
        assertEquals(5, solicitud.getHistorial().size());

        verify(solicitudRepository).obtenerPorCodigo(any(CodigoSolicitud.class));
        verify(solicitudRepository).guardar(solicitud);
    }

    @Test
    void debeLanzarExcepcionCuandoSolicitudNoExiste() {
        // Arrange
        when(solicitudRepository.obtenerPorCodigo(any(CodigoSolicitud.class)))
                .thenThrow(new ExcepcionDeSolicitudNoEncontrada(new CodigoSolicitud("999")));

        // Act & Assert
        assertThrows(ExcepcionDeSolicitudNoEncontrada.class,
                () -> useCase.ejecutar("999", "Observación de cierre"));

        verify(solicitudRepository, never()).guardar(any());
    }

    @Test
    void debeLanzarExcepcionDeDominioCuandoSolicitudNoEstaAtendida() {
        // Arrange
        Solicitud solicitudRegistrada = crearSolicitudRegistrada();
        when(solicitudRepository.obtenerPorCodigo(any(CodigoSolicitud.class)))
                .thenReturn(solicitudRegistrada);

        // Act & Assert
        ExcepcionDeReglaDeDominio ex = assertThrows(ExcepcionDeReglaDeDominio.class,
                () -> useCase.ejecutar("002", "Intento de cierre inválido"));

        assertEquals("Solo se puede cerrar una solicitud atendida", ex.getMessage());

        assertEquals(EstadoDeSolicitud.REGISTRADA, solicitudRegistrada.getEstado());
        verify(solicitudRepository, never()).guardar(any());
    }
}