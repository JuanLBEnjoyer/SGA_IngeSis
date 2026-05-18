package co.edu.uniquindio.proyecto.application.usecase;

import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultarResponsablesUseCase {

    private final UsuarioRepository usuarioRepository;

    public List<Usuario> ejecutar() {
        return usuarioRepository.obtenerResponsables();
    }
}
