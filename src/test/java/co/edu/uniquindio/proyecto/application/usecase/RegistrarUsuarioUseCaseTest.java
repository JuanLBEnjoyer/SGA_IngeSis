package co.edu.uniquindio.proyecto.application.usecase;

import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.exception.ExcepcionDeReglaDeDominio;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.RolUsuario;
import co.edu.uniquindio.proyecto.domain.valueobject.TipoDeDocumento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrarUsuarioUseCaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RegistrarUsuarioUseCase registrarUsuarioUseCase;

    @BeforeEach
    void setUp() {
    }

    @Test
    void debeRegistrarUsuarioExitosamente() {
        // Arrange
        String numeroDocumento = "123456";
        TipoDeDocumento tipoDocumento = TipoDeDocumento.CEDULA;
        String nombre = "Test User";
        String email = "test@uniquindio.edu.co";
        String password = "password123";
        RolUsuario rol = RolUsuario.ESTUDIANTE;

        when(passwordEncoder.encode(password)).thenReturn("encryptedPassword");

        // Act
        registrarUsuarioUseCase.ejecutar(numeroDocumento, tipoDocumento, nombre, email, password, rol);

        // Assert
        ArgumentCaptor<Usuario> usuarioCaptor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository, times(1)).guardar(usuarioCaptor.capture(), eq("encryptedPassword"));

        Usuario usuarioGuardado = usuarioCaptor.getValue();
        assertEquals(numeroDocumento, usuarioGuardado.getDocumento().numero());
        assertEquals(tipoDocumento, usuarioGuardado.getDocumento().tipo());
        assertEquals(nombre, usuarioGuardado.getNombre());
        assertEquals(email, usuarioGuardado.getEmail().valor());
        assertEquals(rol, usuarioGuardado.getRol());
    }

    @Test
    void debeLanzarExcepcionPorDatosInvalidos() {
        // Arrange - Invalid email
        String numeroDocumento = "123456";
        TipoDeDocumento tipoDocumento = TipoDeDocumento.CEDULA;
        String nombre = "Test User";
        String emailInvalido = "test@gmail.com"; // Assuming Email VO has validation for @uniquindio.edu.co
        String password = "password123";
        RolUsuario rol = RolUsuario.ESTUDIANTE;

        // Act & Assert
        assertThrows(ExcepcionDeReglaDeDominio.class, () -> {
            registrarUsuarioUseCase.ejecutar(numeroDocumento, tipoDocumento, nombre, emailInvalido, password, rol);
        });

        // Verify that guardar was never called
        verify(usuarioRepository, never()).guardar(any(), anyString());
    }
}
