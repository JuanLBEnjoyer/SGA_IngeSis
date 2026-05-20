package co.edu.uniquindio.proyecto.domain.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import co.edu.uniquindio.proyecto.domain.exception.ExcepcionDeReglaDeDominio;
import co.edu.uniquindio.proyecto.domain.valueobject.*;
import lombok.Getter;

@Getter
public class Solicitud {

    private final CodigoSolicitud codigo;
    private final String descripcion;
    private final Usuario solicitante;
    private TipoDeSolicitud tipo;
    private EstadoDeSolicitud estado;
    private PrioridadDeSolicitud prioridad;
    private Usuario responsable;
    private final List<RegistroHistorial> historial = new ArrayList<>();

    public Solicitud(CodigoSolicitud codigo, String descripcion, Usuario solicitante, TipoDeSolicitud tipo) {
        if (codigo == null) {
            throw new ExcepcionDeReglaDeDominio("El codigo no puede ser nulo");
        }
        this.codigo = codigo;
        if (descripcion == null || descripcion.isBlank()) {
            throw new ExcepcionDeReglaDeDominio("La descripcion no puede estar vacia");
        }
        this.descripcion = descripcion;
        if (solicitante == null) {
            throw new ExcepcionDeReglaDeDominio("El solicitante no puede estar vacio");
        }
        this.solicitante = solicitante;
        if (tipo == null) {
            throw new ExcepcionDeReglaDeDominio("El tipo de solicitud no puede estar vacio");
        }
        this.tipo = tipo;
        this.estado = EstadoDeSolicitud.REGISTRADA;
        this.historial.add(new RegistroHistorial("Solicitud registrada", LocalDateTime.now(), this.estado));
    }

    // Constructor privado para reconstrucción desde base de datos.

    private Solicitud(CodigoSolicitud codigo, String descripcion, Usuario solicitante, TipoDeSolicitud tipo,
            EstadoDeSolicitud estado, PrioridadDeSolicitud prioridad, Usuario responsable,
            List<RegistroHistorial> historialExistente) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.solicitante = solicitante;
        this.tipo = tipo;
        this.estado = estado;
        this.prioridad = prioridad;
        this.responsable = responsable;
        if (historialExistente != null) {
            this.historial.addAll(historialExistente);
        }
    }

    public static Solicitud reconstruirDesdeDB(CodigoSolicitud codigo, String descripcion, Usuario solicitante,
            TipoDeSolicitud tipo, EstadoDeSolicitud estado, PrioridadDeSolicitud prioridad, Usuario responsable,
            List<RegistroHistorial> historialExistente) {
        return new Solicitud(codigo, descripcion, solicitante, tipo, estado, prioridad, responsable,
                historialExistente);
    }

    public void clasificar(PrioridadDeSolicitud prioridad, String justificacion) {
        if (justificacion == null || justificacion.isBlank()) {
            throw new ExcepcionDeReglaDeDominio("La justificacion no puede estar vacia");
        }
        if (this.estado != EstadoDeSolicitud.REGISTRADA) {
            throw new ExcepcionDeReglaDeDominio("Solo se puede clasificar una solicitud registrada");
        }
        this.prioridad = prioridad;
        this.estado = EstadoDeSolicitud.CLASIFICADA;
        this.historial
                .add(new RegistroHistorial("Solicitud clasificada: " + justificacion, LocalDateTime.now(),
                        this.estado));
    }

    public void asignarResponsable(Usuario responsable) {
        if (this.estado != EstadoDeSolicitud.CLASIFICADA) {
            throw new ExcepcionDeReglaDeDominio("Solo se puede asignar un responsable a una solicitud clasificada");
        }
        this.responsable = responsable;
        this.estado = EstadoDeSolicitud.EN_ATENCION;
        this.historial.add(new RegistroHistorial("Solicitud asignada a responsable", LocalDateTime.now(), this.estado));
    }

    public void atender(String observacion) {
        if (this.estado != EstadoDeSolicitud.EN_ATENCION) {
            throw new ExcepcionDeReglaDeDominio("Solo se puede atender una solicitud en atencion");
        }
        this.estado = EstadoDeSolicitud.ATENDIDA;
        String mensaje = observacion != null && !observacion.isBlank() ? "Solicitud atendida: " + observacion
                : "Solicitud atendida";
        this.historial.add(new RegistroHistorial(mensaje, LocalDateTime.now(), this.estado));
    }

    public void cerrar(String justificacion) {
        if (this.estado != EstadoDeSolicitud.ATENDIDA) {
            throw new ExcepcionDeReglaDeDominio("Solo se puede cerrar una solicitud atendida");
        }
        this.estado = EstadoDeSolicitud.CERRADA;
        String mensaje = justificacion != null && !justificacion.isBlank() ? "Solicitud cerrada: " + justificacion
                : "Solicitud cerrada";
        this.historial
                .add(new RegistroHistorial(mensaje, LocalDateTime.now(), this.estado));
    }

    public void rechazarAtencion(String justificacion) {
        if (this.estado != EstadoDeSolicitud.ATENDIDA) {
            throw new ExcepcionDeReglaDeDominio("Solo se puede rechazar la atención de una solicitud atendida");
        }
        if (justificacion == null || justificacion.isBlank()) {
            throw new ExcepcionDeReglaDeDominio("Debe proporcionar una justificación para rechazar la atención");
        }
        this.estado = EstadoDeSolicitud.EN_ATENCION;
        this.historial.add(new RegistroHistorial("Atención rechazada por el estudiante: " + justificacion,
                LocalDateTime.now(), this.estado));
    }

}
