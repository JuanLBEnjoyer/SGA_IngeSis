package co.edu.uniquindio.proyecto.domain.repository;

import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.valueobject.Documento;

public interface UsuarioRepository {
    Usuario findByDocumento(Documento documento);

    void save(Usuario usuario);
}
