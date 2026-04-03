package co.edu.uniquindio.proyecto.infrastructure.repository;

import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.exception.ExcepcionDeUsuarioNoEncontrado;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.Documento;

import java.util.HashMap;
import java.util.Map;

public class UsuarioRepositoryEnMemoria implements UsuarioRepository {

    private final Map<Documento, Usuario> usuarios = new HashMap<>();

    @Override
    public Usuario obtenerPorDocumento(Documento documento) {
        Usuario usuario = usuarios.get(documento);
        if (usuario == null) {
            throw new ExcepcionDeUsuarioNoEncontrado(documento);
        }
        return usuario;
    }

    @Override
    public void guardar(Usuario usuario) {
        usuarios.put(usuario.getDocumento(), usuario);
    }
}
