package co.edu.uniquindio.proyecto.application.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO para asignar un responsable a una solicitud.
 * Mapea a: AsignarResponsableUseCase
 *
 * Precondición del dominio: la solicitud debe estar en estado CLASIFICADA.
 * Regla de negocio: el responsable no puede tener rol ESTUDIANTE.
 */
public record AsignarResponsableRequest(

        @NotBlank(message = "El número de documento del responsable es obligatorio") String documentoResponsable

) {
}
