package co.edu.uniquindio.proyecto.domain.entity;

import co.edu.uniquindio.proyecto.domain.exception.ExcepcionDeReglaDeDominio;
import co.edu.uniquindio.proyecto.domain.valueobject.*;

public class Solicitud {

    private final CodigoSolicitud codigo;
    private final String descripcion;
    private final String idSolicitante;
    private TipoDeSolicitud tipo;
    private EstadoDeSolicitud estado;
    private PrioridadDeSolicitud prioridad;
    private String idResponsable;

    public Solicitud(CodigoSolicitud codigo, String descripcion, String idSolicitante, TipoDeSolicitud tipo,
            PrioridadDeSolicitud prioridad, String idResponsable) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.idSolicitante = idSolicitante;
        this.tipo = tipo;
        this.estado = EstadoDeSolicitud.REGISTRADA;
        this.prioridad = prioridad;
        this.idResponsable = idResponsable;
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

    public void cerrar() {
        if (this.estado != EstadoDeSolicitud.ATENDIDA) {
            throw new ExcepcionDeReglaDeDominio("Solo se puede cerrar una solicitud atendida");
        }
        this.estado = EstadoDeSolicitud.CERRADA;
    }

}
