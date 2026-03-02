package co.edu.uniquindio.proyecto.domain.valueobject;

import java.time.LocalDateTime;

public record RegistroHistorial(String descripcion, LocalDateTime fecha, EstadoDeSolicitud estadoAsociado) {

}
