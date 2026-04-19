package co.edu.uniquindio.proyecto.application.usecase;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.exception.ExcepcionDeUsuarioNoEncontrado;
import co.edu.uniquindio.proyecto.domain.repository.GeneradorCodigo;
import co.edu.uniquindio.proyecto.domain.repository.SolicitudRepository;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;
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
class CrearSolicitudUseCaseTest {

        @Mock
        private SolicitudRepository solicitudRepository;

        @Mock
        private GeneradorCodigo generadorCodigo;

        @Mock
        private UsuarioRepository usuarioRepository;

        @InjectMocks
        private CrearSolicitudUseCase useCase;

        // ── Fixture helpers ───────────────────────────────────────────────────────

        private Usuario crearEstudiante() {
                return new Usuario(
                                new Documento("1094123456", TipoDeDocumento.CEDULA),
                                "Juan Pablo Galeano",
                                new Email("jpgaleano@uqvirtual.edu.co"),
                                RolUsuario.ESTUDIANTE);
        }

        // ── Tests ─────────────────────────────────────────────────────────────────

        @Test
        void debeCrearSolicitudCuandoDatosValidos() {
                // Arrange
                Usuario estudiante = crearEstudiante();
                CodigoSolicitud codigoGenerado = new CodigoSolicitud("001");

                when(usuarioRepository.obtenerPorDocumento(any(Documento.class)))
                                .thenReturn(estudiante);
                when(generadorCodigo.generar())
                                .thenReturn(codigoGenerado);

                // Act
                Solicitud resultado = useCase.ejecutar(
                                TipoDeSolicitud.REGISTRAR_ASIGNATURA,
                                "Necesito registrar Programación Avanzada",
                                "1094123456",
                                TipoDeDocumento.CEDULA);

                // Assert
                assertNotNull(resultado);
                assertEquals("001", resultado.getCodigo().valor());
                assertEquals(EstadoDeSolicitud.REGISTRADA, resultado.getEstado());
                assertEquals(TipoDeSolicitud.REGISTRAR_ASIGNATURA, resultado.getTipo());

                verify(usuarioRepository).obtenerPorDocumento(any(Documento.class));
                verify(generadorCodigo).generar();
                verify(solicitudRepository).guardar(any(Solicitud.class));
        }

        @Test
        void debeLanzarExcepcionCuandoSolicitanteNoExiste() {
                // Arrange
                when(usuarioRepository.obtenerPorDocumento(any(Documento.class)))
                                .thenThrow(new ExcepcionDeUsuarioNoEncontrado(
                                                new Documento("9999", TipoDeDocumento.CEDULA)));

                // Act & Assert
                assertThrows(ExcepcionDeUsuarioNoEncontrado.class,
                                () -> useCase.ejecutar(
                                                TipoDeSolicitud.HOMOLOGACION,
                                                "Solicito homologación de Algoritmos",
                                                "9999",
                                                TipoDeDocumento.CEDULA));

                verify(generadorCodigo, never()).generar();
                verify(solicitudRepository, never()).guardar(any());
        }

        @Test
        void debeGuardarSolicitudEnRepositorio() {
                // Arrange
                Usuario estudiante = crearEstudiante();
                when(usuarioRepository.obtenerPorDocumento(any(Documento.class)))
                                .thenReturn(estudiante);
                when(generadorCodigo.generar())
                                .thenReturn(new CodigoSolicitud("002"));

                // Act
                useCase.ejecutar(
                                TipoDeSolicitud.CANCELACION_ASIGNATURA,
                                "Necesito cancelar la asignatura de Cálculo",
                                "1094123456",
                                TipoDeDocumento.CEDULA);

                verify(solicitudRepository, times(1)).guardar(any(Solicitud.class));
        }
}
