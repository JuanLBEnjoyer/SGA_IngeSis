package co.edu.uniquindio.proyecto.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para marcar una solicitud como atendida.
 * Mapea a: CambiarEstadoUseCase.atender()
 *
 * Precondición del dominio: la solicitud debe estar en estado EN_ATENCION.
 */
public record AtenderSolicitudRequest(

        @NotBlank(message = "La observación de atención es obligatoria") @Size(max = 500, message = "La observación no puede superar los 500 caracteres") String observacion

) {
}
