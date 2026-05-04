package co.edu.uniquindio.proyecto.application.dto.response;

import co.edu.uniquindio.proyecto.domain.valueobject.EstadoDeSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.PrioridadDeSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.TipoDeSolicitud;

import java.time.LocalDateTime;

/**
 * DTO de respuesta con información completa de una solicitud.
 * Usado en:
 * - POST /api/solicitudes (respuesta al crear)
 * - GET /api/solicitudes/{codigo} (detalle)
 * - PUT /api/solicitudes/{codigo}/clasificar
 * - PUT /api/solicitudes/{codigo}/asignar
 * - PATCH /api/solicitudes/{codigo}/atender
 * - PUT /api/solicitudes/{codigo}/cerrar
 */
public record SolicitudDetalleResponse(
                String codigo,
                TipoDeSolicitud tipo,
                String descripcion,
                EstadoDeSolicitud estado,
                PrioridadDeSolicitud prioridad,
                UsuarioResumenResponse solicitante,
                UsuarioResumenResponse responsable,
                LocalDateTime fechaCreacion,
                int totalRegistrosHistorial) {
}