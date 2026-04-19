package co.edu.uniquindio.proyecto.infrastructure.persistence.mapper;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.*;
import co.edu.uniquindio.proyecto.infrastructure.persistence.entity.RegistroHistorialEmbeddable;
import co.edu.uniquindio.proyecto.infrastructure.persistence.entity.SolicitudEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper de persistencia entre la entidad de DOMINIO y la entidad JPA.
 *
 * Enfoque híbrido:
 * - Domain → Entity: MapStruct genera la implementación automáticamente.
 * - Entity → Domain: método manual porque requiere:
 * 1. Buscar Usuario en el repositorio
 * 2. Reconstruir Value Objects (CodigoSolicitud, Documento)
 * 3. Reconstruir el historial desde los embeddables
 */
@Mapper(componentModel = "spring")
public interface SolicitudPersistenceMapper {

    // ─── Domain → Entity ─────────────────

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "codigo", source = "codigo.valor")
    @Mapping(target = "tipo", source = "tipo")
    @Mapping(target = "descripcion", source = "descripcion")
    @Mapping(target = "estado", source = "estado")
    @Mapping(target = "prioridad", source = "prioridad")
    @Mapping(target = "solicitanteDocumento", source = "solicitante.documento.numero")
    @Mapping(target = "solicitanteTipoDocumento", source = "solicitante.documento.tipo")
    @Mapping(target = "responsableDocumento", source = "responsable.documento.numero")
    @Mapping(target = "responsableTipoDocumento", source = "responsable.documento.tipo")
    @Mapping(target = "historial", source = "historial")
    SolicitudEntity toEntity(Solicitud solicitud);

    // ─── Historial: RegistroHistorial → Embeddable ─────────────────────────────

    @Mapping(target = "descripcion", source = "descripcion")
    @Mapping(target = "fecha", source = "fecha")
    @Mapping(target = "estadoAsociado", source = "estadoAsociado")
    RegistroHistorialEmbeddable toEmbeddable(RegistroHistorial registro);

    List<RegistroHistorialEmbeddable> toEmbeddableList(List<RegistroHistorial> registros);

    // ─── Entity → Domain ─────────────────────
    default Solicitud toDomain(SolicitudEntity entity, UsuarioRepository usuarioRepository) {
        if (entity == null)
            return null;

        // 1. Reconstruir Value Objects del solicitante
        Documento docSolicitante = new Documento(
                entity.getSolicitanteDocumento(),
                TipoDeDocumento.valueOf(entity.getSolicitanteTipoDocumento()));
        Usuario solicitante = usuarioRepository.obtenerPorDocumento(docSolicitante);

        // 2. Reconstruir responsable
        Usuario responsable = null;
        if (entity.getResponsableDocumento() != null) {
            Documento docResponsable = new Documento(
                    entity.getResponsableDocumento(),
                    TipoDeDocumento.valueOf(entity.getResponsableTipoDocumento()));
            responsable = usuarioRepository.obtenerPorDocumento(docResponsable);
        }

        // 3. Reconstruir historial desde embeddables
        List<RegistroHistorial> historial = entity.getHistorial().stream()
                .map(e -> new RegistroHistorial(e.getDescripcion(), e.getFecha(), e.getEstadoAsociado()))
                .collect(Collectors.toList());

        // 4. Usar factory method de reconstrucción para evitar pasar por validaciones
        // de creación
        return Solicitud.reconstruirDesdeDB(
                new CodigoSolicitud(entity.getCodigo()),
                entity.getDescripcion(),
                solicitante,
                entity.getTipo(),
                entity.getEstado(),
                entity.getPrioridad(),
                responsable,
                historial);
    }
}