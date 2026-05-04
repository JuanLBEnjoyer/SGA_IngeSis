package co.edu.uniquindio.proyecto.application.usecase;

import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.Documento;
import co.edu.uniquindio.proyecto.domain.valueobject.Email;
import co.edu.uniquindio.proyecto.domain.valueobject.RolUsuario;
import co.edu.uniquindio.proyecto.domain.valueobject.TipoDeDocumento;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegistrarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void ejecutar(String numeroDocumento, TipoDeDocumento tipoDocumento, String nombre, String email,
            String password, RolUsuario rol) {
        Documento documento = new Documento(numeroDocumento, tipoDocumento);
        Email emailVO = new Email(email);

        Usuario usuario = new Usuario(
                documento,
                nombre,
                emailVO,
                rol);

        String passwordEncriptado = passwordEncoder.encode(password);
        usuarioRepository.guardar(usuario, passwordEncriptado);
    }
}
