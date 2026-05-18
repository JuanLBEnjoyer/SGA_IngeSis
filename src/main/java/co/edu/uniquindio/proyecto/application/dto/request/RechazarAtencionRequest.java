package co.edu.uniquindio.proyecto.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RechazarAtencionRequest(
        @NotBlank(message = "La justificación es obligatoria") String justificacion
) {
}
