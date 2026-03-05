package co.edu.uniquindio.proyecto.domain.valueobject;

import co.edu.uniquindio.proyecto.domain.exception.ExcepcionDeReglaDeDominio;
import java.time.LocalDateTime;

public record RegistroHistorial(String descripcion, LocalDateTime fecha, EstadoDeSolicitud estadoAsociado) {

    public RegistroHistorial {
        if (descripcion == null || descripcion.isBlank()) {
            throw new ExcepcionDeReglaDeDominio("La descripcion no puede estar vacia");
        }
        if (fecha == null) {
            throw new ExcepcionDeReglaDeDominio("La fecha no puede ser nula");
        }
        if (estadoAsociado == null) {
            throw new ExcepcionDeReglaDeDominio("El estado asociado no puede ser nulo");
        }
    }
}
