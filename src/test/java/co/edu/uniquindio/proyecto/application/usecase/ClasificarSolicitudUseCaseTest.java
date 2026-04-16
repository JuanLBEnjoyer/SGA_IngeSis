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
class ClasificarSolicitudUseCaseTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    @InjectMocks
    private ClasificarSolicitudUseCase useCase;

    private Solicitud crearSolicitudRegistrada() {
        Usuario solicitante = new Usuario(
                new Documento("123456", TipoDeDocumento.CEDULA),
                "Juan Perez",
                new Email("juan.perez@uqvirtual.edu.co"),
                RolUsuario.ESTUDIANTE);
        return new Solicitud(
                new CodigoSolicitud("001"),
                "Registro de materias primer semestre",
                solicitante,
                TipoDeSolicitud.REGISTRAR_ASIGNATURA);
    }

    @Test
    void debeClasificarSolicitudRegistrada() {
        // Arrange
        Solicitud solicitud = crearSolicitudRegistrada();
        when(solicitudRepository.obtenerPorCodigo(any(CodigoSolicitud.class)))
                .thenReturn(solicitud);

        // Act
        useCase.ejecutar("001", PrioridadDeSolicitud.ALTO, "Inicio de semestre urgente");

        // Assert
        assertEquals(EstadoDeSolicitud.CLASIFICADA, solicitud.getEstado());
        assertEquals(PrioridadDeSolicitud.ALTO, solicitud.getPrioridad());

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
                () -> useCase.ejecutar("999", PrioridadDeSolicitud.MEDIO, "Justificación"));

        verify(solicitudRepository, never()).guardar(any());
    }

    @Test
    void debeLanzarExcepcionDeDominioCuandoSolicitudNoEstaRegistrada() {
        // Arrange
        Solicitud solicitud = crearSolicitudRegistrada();
        solicitud.clasificar(PrioridadDeSolicitud.BAJO, "Primera clasificación");
        when(solicitudRepository.obtenerPorCodigo(any(CodigoSolicitud.class)))
                .thenReturn(solicitud);

        // Act & Assert
        ExcepcionDeReglaDeDominio ex = assertThrows(ExcepcionDeReglaDeDominio.class,
                () -> useCase.ejecutar("001", PrioridadDeSolicitud.ALTO, "Reclasificación"));

        assertEquals("Solo se puede clasificar una solicitud registrada", ex.getMessage());
        verify(solicitudRepository, never()).guardar(any());
    }
}
