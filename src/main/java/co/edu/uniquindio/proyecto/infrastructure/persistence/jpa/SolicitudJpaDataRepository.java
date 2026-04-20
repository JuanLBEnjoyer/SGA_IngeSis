package co.edu.uniquindio.proyecto.infrastructure.persistence.jpa;

import co.edu.uniquindio.proyecto.domain.valueobject.EstadoDeSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.PrioridadDeSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.TipoDeSolicitud;
import co.edu.uniquindio.proyecto.infrastructure.persistence.entity.SolicitudEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    List<SolicitudEntity> findByEstado(EstadoDeSolicitud estado);

    List<SolicitudEntity> findByTipoAndEstado(TipoDeSolicitud tipo, EstadoDeSolicitud estado);

    List<SolicitudEntity> findByDescripcionContainingIgnoreCase(String keyword);

    List<SolicitudEntity> findByPrioridad(PrioridadDeSolicitud prioridad);

    List<SolicitudEntity> findBySolicitanteDocumentoOrderByIdDesc(String documento);

    boolean existsByCodigo(String codigo);

    List<SolicitudEntity> findByTipoOrderByPrioridadDesc(TipoDeSolicitud tipo);

    @Query("SELECT s FROM SolicitudEntity s WHERE s.tipo = :tipo AND s.estado = :estado")
    List<SolicitudEntity> findByTipoAndEstadoJpql(@Param("tipo") TipoDeSolicitud tipo,
            @Param("estado") EstadoDeSolicitud estado);

    @Query("SELECT s FROM SolicitudEntity s WHERE LOWER(s.descripcion) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<SolicitudEntity> findByDescripcionContainingJpql(@Param("keyword") String keyword);

    @Query("SELECT s FROM SolicitudEntity s WHERE s.estado IN :estados")
    List<SolicitudEntity> findByEstadosInJpql(@Param("estados") List<EstadoDeSolicitud> estados);

    @Query("SELECT s FROM SolicitudEntity s LEFT JOIN FETCH s.historial WHERE s.codigo = :codigo")
    Optional<SolicitudEntity> findByCodigoWithHistorialJpql(@Param("codigo") String codigo);

    @Query("SELECT s FROM SolicitudEntity s WHERE (:codigo IS NULL OR s.codigo = :codigo)")
    List<SolicitudEntity> findByCodigoCondicionalJpql(@Param("codigo") String codigo);

    @Query(value = "SELECT estado, COUNT(*) FROM solicitudes GROUP BY estado", nativeQuery = true)
    List<Object[]> countAgrupadoPorEstadoNativo();

    Page<SolicitudEntity> findByEstadoNot(EstadoDeSolicitud estado, Pageable pageable);
}
