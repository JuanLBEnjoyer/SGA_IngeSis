package co.edu.uniquindio.proyecto.infrastructure.rest.mapper;

import co.edu.uniquindio.proyecto.application.dto.response.*;
import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.RegistroHistorial;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = { UsuarioMapper.class })
public interface SolicitudMapper {

    // ── Solicitud → SolicitudDetalleResponse ──────────────────────────────────
    @Mapping(target = "codigo", source = "codigo.valor")
    @Mapping(target = "tipo", source = "tipo")
    @Mapping(target = "descripcion", source = "descripcion")
    @Mapping(target = "estado", source = "estado")
    @Mapping(target = "prioridad", source = "prioridad")
    @Mapping(target = "solicitante", source = "solicitante")
    @Mapping(target = "responsable", source = "responsable")
    @Mapping(target = "fechaCreacion", expression = "java(solicitud.getHistorial().isEmpty() ? null : solicitud.getHistorial().get(0).fecha())")
    @Mapping(target = "totalRegistrosHistorial", expression = "java(solicitud.getHistorial().size())")
    SolicitudDetalleResponse toDetalleResponse(Solicitud solicitud);

    // ── Solicitud → SolicitudResumenResponse ──────────────────────────────────
    @Mapping(target = "codigo", source = "codigo.valor")
    @Mapping(target = "tipo", source = "tipo")
    @Mapping(target = "descripcionBreve", expression = "java(solicitud.getDescripcion().length() > 100 ? solicitud.getDescripcion().substring(0, 100) : solicitud.getDescripcion())")
    @Mapping(target = "estado", source = "estado")
    @Mapping(target = "prioridad", source = "prioridad")
    @Mapping(target = "nombreSolicitante", source = "solicitante.nombre")
    @Mapping(target = "nombreResponsable", source = "responsable.nombre")
    @Mapping(target = "fechaCreacion", expression = "java(solicitud.getHistorial().isEmpty() ? null : solicitud.getHistorial().get(0).fecha())")
    SolicitudResumenResponse toResumenResponse(Solicitud solicitud);

    List<SolicitudResumenResponse> toResumenResponseList(List<Solicitud> solicitudes);

    // ── RegistroHistorial → RegistroHistorialResponse ─────────────────────────
    @Mapping(target = "descripcion", source = "descripcion")
    @Mapping(target = "fecha", source = "fecha")
    @Mapping(target = "estadoAsociado", source = "estadoAsociado")
    RegistroHistorialResponse toHistorialResponse(RegistroHistorial registro);

    List<RegistroHistorialResponse> toHistorialResponseList(List<RegistroHistorial> registros);
}