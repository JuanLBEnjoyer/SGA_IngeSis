package co.edu.uniquindio.proyecto.domain.entity;

import co.edu.uniquindio.proyecto.domain.exception.ExcepcionDeReglaDeDominio;
import co.edu.uniquindio.proyecto.domain.valueobject.*;
import lombok.Getter;

@Getter
public class Solicitud {

    private final CodigoSolicitud codigo;
    private final String descripcion;
    private final String idSolicitante;
    private TipoDeSolicitud tipo;
    private EstadoDeSolicitud estado;
    private PrioridadDeSolicitud prioridad;
    private String idResponsable;

    public Solicitud(CodigoSolicitud codigo, String descripcion, String idSolicitante, TipoDeSolicitud tipo) {
        this.codigo = codigo;
        if (descripcion == null || descripcion.isBlank()) {
            throw new ExcepcionDeReglaDeDominio("La descripcion no puede estar vacia");
        }
        this.descripcion = descripcion;
        this.idSolicitante = idSolicitante;
        this.estado = EstadoDeSolicitud.REGISTRADA;
    }

    public void clasificarSolicitud(TipoDeSolicitud tipo, PrioridadDeSolicitud prioridad) {
        if (this.estado != EstadoDeSolicitud.REGISTRADA) {
            throw new ExcepcionDeReglaDeDominio("Solo se puede clasificar una solicitud registrada");
        }
        this.tipo = tipo;
        this.prioridad = prioridad;
        this.estado = EstadoDeSolicitud.CLASIFICADA;
    }

    public void asignarResponsable(String idResponsable) {
        if (this.estado != EstadoDeSolicitud.CLASIFICADA) {
            throw new ExcepcionDeReglaDeDominio("Solo se puede asignar un responsable a una solicitud clasificada");
        }
        this.idResponsable = idResponsable;
        this.estado = EstadoDeSolicitud.EN_ATENCION;
    }

    public void atender() {
        if (this.estado != EstadoDeSolicitud.EN_ATENCION) {
            throw new ExcepcionDeReglaDeDominio("Solo se puede atender una solicitud en atencion");
        }
        this.estado = EstadoDeSolicitud.ATENDIDA;
    }

    public void cerrar() {
        if (this.estado != EstadoDeSolicitud.ATENDIDA) {
            throw new ExcepcionDeReglaDeDominio("Solo se puede cerrar una solicitud atendida");
        }
        this.estado = EstadoDeSolicitud.CERRADA;
    }

}
