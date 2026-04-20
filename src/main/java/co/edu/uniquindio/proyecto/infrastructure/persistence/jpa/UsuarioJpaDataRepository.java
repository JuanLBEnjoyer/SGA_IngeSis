package co.edu.uniquindio.proyecto.infrastructure.persistence.jpa;

import co.edu.uniquindio.proyecto.infrastructure.persistence.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Interfaz Spring Data JPA para UsuarioEntity.
 * Spring genera la implementación SQL automáticamente.
 * Expuesto a infraestructura transversal.
 */
public interface UsuarioJpaDataRepository extends JpaRepository<UsuarioEntity, UUID> {

    Optional<UsuarioEntity> findByNumeroDocumentoAndTipoDocumento(String numeroDocumento, String tipoDocumento);

    Optional<UsuarioEntity> findByEmail(String email);
}
