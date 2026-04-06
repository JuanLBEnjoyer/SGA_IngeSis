package co.edu.uniquindio.proyecto.application.dto.request;

import co.edu.uniquindio.proyecto.domain.valueobject.PrioridadDeSolicitud;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO para clasificar una solicitud existente.
 * Mapea a: CambiarEstadoUseCase.clasificar()
 *
 * Precondición del dominio: la solicitud debe estar en estado REGISTRADA.
 */
public record ClasificarSolicitudRequest(

        @NotNull(message = "La prioridad es obligatoria") PrioridadDeSolicitud prioridad,

        @NotBlank(message = "La justificación es obligatoria") @Size(min = 10, max = 500, message = "La justificación debe tener entre 10 y 500 caracteres") String justificacion

) {
}
