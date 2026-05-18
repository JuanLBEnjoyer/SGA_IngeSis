package co.edu.uniquindio.proyecto.infrastructure.persistence.jpa;

import co.edu.uniquindio.proyecto.infrastructure.persistence.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioJpaDataRepository extends JpaRepository<UsuarioEntity, UUID> {

    Optional<UsuarioEntity> findByNumeroDocumentoAndTipoDocumento(String numeroDocumento, String tipoDocumento);

    Optional<UsuarioEntity> findByEmail(String email);

    java.util.List<UsuarioEntity> findByRolIn(java.util.List<co.edu.uniquindio.proyecto.domain.valueobject.RolUsuario> roles);
}
