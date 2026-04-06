package co.edu.uniquindio.proyecto.application.dto.request;

import co.edu.uniquindio.proyecto.domain.valueobject.TipoDeSolicitud;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO para registrar una nueva solicitud.
 * Mapea a: CrearSolicitudUseCase
 *
 * NO incluir: codigo, estado, historial
 * Estos los genera y controla el dominio automáticamente.
 */
public record CrearSolicitudRequest(

                @NotNull(message = "El tipo de solicitud es obligatorio") TipoDeSolicitud tipo,

                @NotBlank(message = "La descripción es obligatoria") @Size(min = 10, max = 500, message = "La descripción debe tener entre 10 y 500 caracteres") String descripcion,

                @NotBlank(message = "El número de documento del solicitante es obligatorio") String documentoSolicitante

) {
}