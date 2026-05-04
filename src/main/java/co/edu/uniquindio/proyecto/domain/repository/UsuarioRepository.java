package co.edu.uniquindio.proyecto.domain.repository;

import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.valueobject.Documento;

public interface UsuarioRepository {
    Usuario obtenerPorDocumento(Documento documento);

    void guardar(Usuario usuario, String passwordEncriptado);

}
