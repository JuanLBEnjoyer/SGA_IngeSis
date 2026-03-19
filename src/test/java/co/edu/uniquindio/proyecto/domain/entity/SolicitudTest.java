package co.edu.uniquindio.proyecto.domain.entity;

import co.edu.uniquindio.proyecto.domain.exception.ExcepcionDeReglaDeDominio;
import co.edu.uniquindio.proyecto.domain.valueobject.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SolicitudTest {

        private final CodigoSolicitud codigo = new CodigoSolicitud("001");
        private final Documento documento = new Documento("123456", TipoDeDocumento.CEDULA);
        private final Usuario solicitante = new Usuario(documento, "Juan Perez", new Email("juan.perez@uqvirtual.edu.co"), RolUsuario.ESTUDIANTE);

        @Test
        void debeCrearSolicitudValida() {
                Solicitud solicitud = new Solicitud(
                                codigo,
                                "Registro de materias",
                                solicitante,
                                TipoDeSolicitud.REGISTRAR_ASIGNATURA);
                assertNotNull(solicitud);
                assertEquals(EstadoDeSolicitud.REGISTRADA, solicitud.getEstado());
                assertEquals("Registro de materias", solicitud.getDescripcion());
                assertEquals(1, solicitud.getHistorial().size());
        }

        @Test
        void noDebeCrearSolicitudConDescripcionVacia() {
                Exception ex = assertThrows(ExcepcionDeReglaDeDominio.class,
                                () -> new Solicitud(
                                                codigo,
                                                "",
                                                solicitante,
                                                TipoDeSolicitud.CONSULTA_ACADEMICA));
                assertEquals("La descripcion no puede estar vacia", ex.getMessage());
        }

        @Test
        void noDebeCrearSolicitudConDescripcionNula() {
                Exception ex = assertThrows(ExcepcionDeReglaDeDominio.class,
                                () -> new Solicitud(
                                                codigo,
                                                null,
                                                solicitante,
                                                TipoDeSolicitud.CONSULTA_ACADEMICA));
                assertEquals("La descripcion no puede estar vacia", ex.getMessage());
        }

        @Test
        void noDebeCrearSolicitudConTipoNulo() {
                Exception ex = assertThrows(ExcepcionDeReglaDeDominio.class,
                                () -> new Solicitud(
                                                codigo,
                                                "Problema con el sistema",
                                                solicitante,
                                                null));
                assertEquals("El tipo de solicitud no puede estar vacio", ex.getMessage());
        }

        @Test
    void noDebeCrearSolicitudConSolicitanteNulo() {
        Exception ex = assertThrows(ExcepcionDeReglaDeDominio.class,
                () -> new Solicitud(
                        codigo,
                        "Registro de materias",
                        null,
                        TipoDeSolicitud.REGISTRAR_ASIGNATURA
                ));
        assertEquals("El solicitante no puede estar vacio", ex.getMessage());
    }
    @Test
    void debeClasificarSolicitudRegistrada() {
        Solicitud solicitud = new Solicitud(
                codigo,
                "Registro de materias",
                solicitante,
                TipoDeSolicitud.REGISTRAR_ASIGNATURA
        );
        solicitud.clasificar(PrioridadDeSolicitud.MEDIO, "Falta de cupo");
        assertEquals(EstadoDeSolicitud.CLASIFICADA, solicitud.getEstado());
        assertEquals(PrioridadDeSolicitud.MEDIO, solicitud.getPrioridad());
        assertEquals(2, solicitud.getHistorial().size());
    }
    @Test
    void noDebeClasificarSolicitudConJustificacionVacia() {
        Solicitud solicitud = new Solicitud(
                codigo,
                "Registro de materias",
                solicitante,
                TipoDeSolicitud.REGISTRAR_ASIGNATURA
        );
        Exception ex = assertThrows(ExcepcionDeReglaDeDominio.class,
                () -> solicitud.clasificar(PrioridadDeSolicitud.MEDIO, ""));
        assertEquals("La justificacion no puede estar vacia", ex.getMessage());
    }

        @Test
    void noDebeClasificarSolicitudQueNoEsteRegistrada() {
        Solicitud solicitud = new Solicitud(
                codigo,
                "Registro de materias",
                solicitante,
                TipoDeSolicitud.REGISTRAR_ASIGNATURA
        );
        solicitud.clasificar(PrioridadDeSolicitud.MEDIO, "Falta de cupo");
        Exception ex = assertThrows(ExcepcionDeReglaDeDominio.class,
                () -> solicitud.clasificar(PrioridadDeSolicitud.MEDIO, "Falta de cupo"));
        assertEquals("Solo se puede clasificar una solicitud registrada", ex.getMessage());
    }

        @Test
    void debeAsignarResponsableASolicitudClasificada() {
        Usuario responsable = new Usuario(new Documento("789456", TipoDeDocumento.CEDULA), "Ana Gomez", new Email("ana.gomez@uqvirtual.edu.co"), RolUsuario.ADMINISTRATIVO);
        Solicitud solicitud = new Solicitud(
                codigo,
                "Registro de materias",
                solicitante,
                TipoDeSolicitud.REGISTRAR_ASIGNATURA
        );
        solicitud.clasificar(PrioridadDeSolicitud.MEDIO, "Falta de cupo");
        solicitud.asignarResponsable(responsable);
        assertEquals(EstadoDeSolicitud.EN_ATENCION, solicitud.getEstado());
        assertEquals(responsable, solicitud.getResponsable());
    }

        @Test
    void noDebeAsignarResponsableSiNoEstaClasificada() {
        Usuario responsable = new Usuario(new Documento("789456", TipoDeDocumento.CEDULA), "Ana Gomez", new Email("ana.gomez@uqvirtual.edu.co"), RolUsuario.ADMINISTRATIVO);
        Solicitud solicitud = new Solicitud(
                codigo,
                "Registro de materias",
                solicitante,
                TipoDeSolicitud.REGISTRAR_ASIGNATURA
        );
        Exception ex = assertThrows(ExcepcionDeReglaDeDominio.class,
                () -> solicitud.asignarResponsable(responsable));
        assertEquals("Solo se puede asignar un responsable a una solicitud clasificada", ex.getMessage());
    }

        @Test
    void debeAtenderSolicitudEnAtencion() {
        Usuario responsable = new Usuario(new Documento("789456", TipoDeDocumento.CEDULA), "Ana Gomez", new Email("ana.gomez@uqvirtual.edu.co"), RolUsuario.ADMINISTRATIVO);
        Solicitud solicitud = new Solicitud(
                codigo,
                "Registro de materias",
                solicitante,
                TipoDeSolicitud.REGISTRAR_ASIGNATURA
        );
        solicitud.clasificar(PrioridadDeSolicitud.MEDIO, "Falta de cupo");
        solicitud.asignarResponsable(responsable);
        solicitud.atender();
        assertEquals(EstadoDeSolicitud.ATENDIDA, solicitud.getEstado());
        assertEquals(4, solicitud.getHistorial().size());
    }

        @Test
        void noDebeAtenderSolicitudQueNoEsteEnAtencion() {
                Solicitud solicitud = new Solicitud(
                                codigo,
                                "Registro de materias",
                                solicitante,
                                TipoDeSolicitud.REGISTRAR_ASIGNATURA);
                Exception ex = assertThrows(ExcepcionDeReglaDeDominio.class,
                                solicitud::atender);
                assertEquals("Solo se puede atender una solicitud en atencion", ex.getMessage());
        }

        @Test
    void debeCerrarSolicitudAtendida() {
        Usuario responsable = new Usuario(new Documento("789456", TipoDeDocumento.CEDULA), "Ana Gomez", new Email("ana.gomez@uqvirtual.edu.co"), RolUsuario.ADMINISTRATIVO);
        Solicitud solicitud = new Solicitud(
                codigo,
                "Registro de materias",
                solicitante,
                TipoDeSolicitud.REGISTRAR_ASIGNATURA
        );
        solicitud.clasificar(PrioridadDeSolicitud.MEDIO, "Falta de cupo");
        solicitud.asignarResponsable(responsable);
        solicitud.atender();
        solicitud.cerrar();
        assertEquals(EstadoDeSolicitud.CERRADA, solicitud.getEstado());
        assertEquals(5, solicitud.getHistorial().size());
    }

        @Test
        void noDebeCerrarSolicitudSiNoEstaAtendida() {
                Solicitud solicitud = new Solicitud(
                                codigo,
                                "Registro de materias",
                                solicitante,
                                TipoDeSolicitud.REGISTRAR_ASIGNATURA);
                Exception ex = assertThrows(ExcepcionDeReglaDeDominio.class,
                                solicitud::cerrar);
                assertEquals("Solo se puede cerrar una solicitud atendida", ex.getMessage());
        }
}