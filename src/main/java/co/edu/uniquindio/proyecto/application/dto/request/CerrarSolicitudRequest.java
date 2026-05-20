package co.edu.uniquindio.proyecto.application.dto.request;

import jakarta.validation.constraints.Size;

/**
 * DTO para cerrar una solicitud (estado final).
 * Mapea a: CerrarSolicitudUseCase
 *
 * Precondición del dominio: la solicitud debe estar en estado ATENDIDA.
 */
public record CerrarSolicitudRequest(

                @Size(max = 500, message = "La observación no puede superar los 500 caracteres") String observacion

) {
}
