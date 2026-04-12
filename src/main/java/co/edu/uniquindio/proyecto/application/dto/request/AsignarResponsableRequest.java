package co.edu.uniquindio.proyecto.application.dto.request;

import co.edu.uniquindio.proyecto.domain.valueobject.TipoDeDocumento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para asignar un responsable a una solicitud.
 * Mapea a: AsignarResponsableUseCase
 *
 * Precondición del dominio: la solicitud debe estar en estado CLASIFICADA.
 * Regla de negocio: el responsable no puede tener rol ESTUDIANTE.
 */
public record AsignarResponsableRequest(

        @NotBlank(message = "El número de documento del responsable es obligatorio") String documentoResponsable,

        @NotNull(message = "El tipo de documento del responsable es obligatorio") TipoDeDocumento tipoDocumentoResponsable) {
}
