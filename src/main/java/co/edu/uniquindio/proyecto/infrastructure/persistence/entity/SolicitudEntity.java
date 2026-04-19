package co.edu.uniquindio.proyecto.infrastructure.persistence.entity;

import co.edu.uniquindio.proyecto.domain.valueobject.EstadoDeSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.PrioridadDeSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.TipoDeSolicitud;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad JPA para persistir Solicitud en H2.
 *
 * ¿Por qué separada de Solicitud del dominio?
 * - JPA requiere constructor vacío → el dominio lo prohíbe
 * - JPA necesita setters → el dominio los prohíbe
 * - Si anotáramos Solicitud del dominio, quedaría acoplada
 *
 * Lombok genera:
 * - @Getter → todos los getters
 * - @Setter → todos los setters
 * - @NoArgsConstructor → constructor vacío requerido por JPA
 */
@Entity
@Table(name = "solicitudes", indexes = {
        @Index(name = "idx_solicitud_codigo", columnList = "codigo", unique = true),
        @Index(name = "idx_solicitud_estado", columnList = "estado"),
        @Index(name = "idx_solicitud_solicitante", columnList = "solicitante_documento")
})
@Getter
@Setter
@NoArgsConstructor
public class SolicitudEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo", unique = true, nullable = false, length = 20)
    private String codigo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 30)
    private TipoDeSolicitud tipo;

    @Column(name = "descripcion", nullable = false, length = 500)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoDeSolicitud estado;

    // Prioridad puede ser null si la solicitud aún no fue clasificada
    @Enumerated(EnumType.STRING)
    @Column(name = "prioridad", length = 10)
    private PrioridadDeSolicitud prioridad;

    // Solicitante — guardamos los dos campos que forman Documento
    @Column(name = "solicitante_documento", nullable = false, length = 50)
    private String solicitanteDocumento;

    @Column(name = "solicitante_tipo_documento", nullable = false, length = 30)
    private String solicitanteTipoDocumento;

    // Responsable — puede ser null si aún no fue asignado
    @Column(name = "responsable_documento", length = 50)
    private String responsableDocumento;

    @Column(name = "responsable_tipo_documento", length = 30)
    private String responsableTipoDocumento;

    /**
     * Historial de cambios de estado.
     *
     * Usamos @ElementCollection porque RegistroHistorial es un Value Object puro:
     * no tiene identidad propia
     * Sus campos se almacenan en una tabla separada con FK a solicitudes.
     *
     * Evita el problema N+1.
     */
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "solicitud_historial", joinColumns = @JoinColumn(name = "solicitud_id"))
    @OrderColumn(name = "orden")
    private List<RegistroHistorialEmbeddable> historial = new ArrayList<>();
}
