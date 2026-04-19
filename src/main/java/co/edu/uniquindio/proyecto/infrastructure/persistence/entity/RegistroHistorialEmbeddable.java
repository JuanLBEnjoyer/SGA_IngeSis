package co.edu.uniquindio.proyecto.infrastructure.persistence.entity;

import co.edu.uniquindio.proyecto.domain.valueobject.EstadoDeSolicitud;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// Embeddable JPA para RegistroHistorial (Value Object del dominio).

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistroHistorialEmbeddable {

    @Column(name = "descripcion", nullable = false, length = 500)
    private String descripcion;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_asociado", nullable = false, length = 20)
    private EstadoDeSolicitud estadoAsociado;
}