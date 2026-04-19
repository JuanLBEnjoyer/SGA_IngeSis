package co.edu.uniquindio.proyecto.infrastructure.persistence.jpa;

import co.edu.uniquindio.proyecto.domain.valueobject.EstadoDeSolicitud;
import co.edu.uniquindio.proyecto.infrastructure.persistence.entity.SolicitudEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

interface SolicitudJpaDataRepository extends JpaRepository<SolicitudEntity, Long> {

    Page<SolicitudEntity> findByEstado(EstadoDeSolicitud estado, Pageable pageable);

    Optional<SolicitudEntity> findByCodigo(String codigo);

    long countByEstado(EstadoDeSolicitud estado);

    List<SolicitudEntity> findBySolicitanteDocumento(String documento);

    @Query("SELECT s FROM SolicitudEntity s WHERE s.estado IN ('REGISTRADA', 'CLASIFICADA')")
    List<SolicitudEntity> findPendientesDeAsignacion();

    /**
     * Devuelve el mayor id existente
     * Se usa para derivar el siguiente código de negocio de forma persistente.
     */
    @Query("SELECT COALESCE(MAX(s.id), 0) FROM SolicitudEntity s")
    long findMaxId();
}
