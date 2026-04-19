package co.edu.uniquindio.proyecto.infrastructure.persistence.jpa;

import co.edu.uniquindio.proyecto.infrastructure.persistence.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Interfaz Spring Data JPA para UsuarioEntity.
 * Spring genera la implementación SQL automáticamente.
 * Package-private: no se expone fuera de infrastructure.
 */
interface UsuarioJpaDataRepository extends JpaRepository<UsuarioEntity, Long> {

    Optional<UsuarioEntity> findByNumeroDocumentoAndTipoDocumento(String numeroDocumento, String tipoDocumento);
}
