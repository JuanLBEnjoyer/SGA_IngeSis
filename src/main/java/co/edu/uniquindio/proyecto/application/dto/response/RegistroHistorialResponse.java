package co.edu.uniquindio.proyecto.application.dto.response;

import co.edu.uniquindio.proyecto.domain.valueobject.EstadoDeSolicitud;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para cada entrada del historial de una solicitud.
 * Usado en: GET /api/solicitudes/{codigo}/historial
 *
 * Mapea directamente desde RegistroHistorial (value object del dominio),
 * pero como DTO independiente para no exponer el value object directamente.
 */
public record RegistroHistorialResponse(
        String descripcion,
        LocalDateTime fecha,
        EstadoDeSolicitud estadoAsociado) {
}
