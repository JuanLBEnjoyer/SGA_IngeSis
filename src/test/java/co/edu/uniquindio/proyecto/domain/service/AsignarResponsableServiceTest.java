package co.edu.uniquindio.proyecto.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import co.edu.uniquindio.proyecto.domain.entity.*;
import co.edu.uniquindio.proyecto.domain.exception.ExcepcionDeReglaDeDominio;
import co.edu.uniquindio.proyecto.domain.valueobject.*;

public class AsignarResponsableServiceTest {

    @Test
    void debeAsignarResponsable() {
        Solicitud solicitud = new Solicitud(
                new CodigoSolicitud("001"),
                "Registro de materias",
                new Documento("123456", TipoDeDocumento.CEDULA),
                TipoDeSolicitud.REGISTRAR_ASIGNATURA);
        solicitud.clasificar(PrioridadDeSolicitud.ALTO);
        Usuario usuario = new Usuario(
                new Documento("123456", TipoDeDocumento.CEDULA), "Estiv Garcia",
                new Email("estivgarcia@uniquindio.edu.co"),
                RolUsuario.ADMINISTRATIVO);
        AsignarResponsableService servicio = new AsignarResponsableService();
        servicio.asignar(solicitud, usuario);

        assertEquals(EstadoDeSolicitud.EN_ATENCION, solicitud.getEstado());

        assertEquals(usuario.getDocumento(), solicitud.getDocumentoResponsable());

        assertEquals(3, solicitud.getHistorial().size());
    }

    @Test
    void noDebeAsignarSiResponsableEsEstudiante() {
        Usuario usuario = new Usuario(
                new Documento("123456", TipoDeDocumento.CEDULA), "Estiv Garcia",
                new Email("estivgarcia@uniquindio.edu.co"),
                RolUsuario.ESTUDIANTE);
        Solicitud solicitud = new Solicitud(
                new CodigoSolicitud("001"),
                "Registro de materias",
                new Documento("123456", TipoDeDocumento.CEDULA),
                TipoDeSolicitud.REGISTRAR_ASIGNATURA);
        solicitud.clasificar(PrioridadDeSolicitud.ALTO);
        AsignarResponsableService servicio = new AsignarResponsableService();
        Exception ex = assertThrows(ExcepcionDeReglaDeDominio.class,
                () -> servicio.asignar(solicitud, usuario));
        assertEquals("Un estudiante no puede ser responsable", ex.getMessage());
    }

    @Test
    void noDebeAsignarSiSolicitudNoEstaClasificada() {
        Solicitud solicitud = new Solicitud(
                new CodigoSolicitud("001"),
                "Registro de materias",
                new Documento("123456", TipoDeDocumento.CEDULA),
                TipoDeSolicitud.REGISTRAR_ASIGNATURA);
        Usuario usuario = new Usuario(
                new Documento("123456", TipoDeDocumento.CEDULA), "Estiv Garcia",
                new Email("estivgarcia@uniquindio.edu.co"),
                RolUsuario.ADMINISTRATIVO);
        AsignarResponsableService servicio = new AsignarResponsableService();
        Exception ex = assertThrows(ExcepcionDeReglaDeDominio.class,
                () -> servicio.asignar(solicitud, usuario));
        assertEquals("Solo se puede asignar un responsable a una solicitud clasificada", ex.getMessage());
    }
}
