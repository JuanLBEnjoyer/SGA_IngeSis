package co.edu.uniquindio.proyecto.application.dto.response;

import co.edu.uniquindio.proyecto.domain.valueobject.EstadoDeSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.PrioridadDeSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.TipoDeSolicitud;

import java.time.LocalDateTime;

/**
 * DTO de respuesta simplificado para listar solicitudes.
 * Usado en: GET /api/solicitudes
 *
 * Es más ligero que SolicitudDetalleResponse porque en un listado
 * no necesitamos todos los datos. Por ejemplo:
 * - No incluimos el historial completo
 * - Del solicitante y responsable solo mostramos el nombre
 * - La descripción se trunca a 100 caracteres
 */
public record SolicitudResumenResponse(
        String codigo,
        TipoDeSolicitud tipo,
        String descripcionBreve,
        EstadoDeSolicitud estado,
        PrioridadDeSolicitud prioridad,
        String nombreSolicitante,
        String nombreResponsable,
        LocalDateTime fechaCreacion) {
}
