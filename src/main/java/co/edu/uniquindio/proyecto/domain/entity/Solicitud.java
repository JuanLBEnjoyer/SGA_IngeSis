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
    private final Documento documentoSolicitante;
    private TipoDeSolicitud tipo;
    private EstadoDeSolicitud estado;
    private PrioridadDeSolicitud prioridad;
    private Documento documentoResponsable;
    private final List<RegistroHistorial> historial = new ArrayList<>();

    public Solicitud(CodigoSolicitud codigo, String descripcion, Documento documentoSolicitante, TipoDeSolicitud tipo) {
        this.codigo = codigo;
        if (descripcion == null || descripcion.isBlank()) {
            throw new ExcepcionDeReglaDeDominio("La descripcion no puede estar vacia");
        }
        this.descripcion = descripcion;
        this.documentoSolicitante = documentoSolicitante;
        if (tipo == null) {
            throw new ExcepcionDeReglaDeDominio("El tipo de solicitud no puede estar vacio");
        }
        this.tipo = tipo;
        this.estado = EstadoDeSolicitud.REGISTRADA;
        this.historial.add(new RegistroHistorial("Solicitud registrada", LocalDateTime.now(), this.estado));
    }

    public void clasificarSolicitud(PrioridadDeSolicitud prioridad) {
        if (this.estado != EstadoDeSolicitud.REGISTRADA) {
            throw new ExcepcionDeReglaDeDominio("Solo se puede clasificar una solicitud registrada");
        }
        this.prioridad = prioridad;
        this.estado = EstadoDeSolicitud.CLASIFICADA;
        this.historial.add(new RegistroHistorial("Solicitud clasificada", LocalDateTime.now(), this.estado));
    }

    public void asignarResponsable(Documento documentoResponsable) {
        if (this.estado != EstadoDeSolicitud.CLASIFICADA) {
            throw new ExcepcionDeReglaDeDominio("Solo se puede asignar un responsable a una solicitud clasificada");
        }
        this.documentoResponsable = documentoResponsable;
        this.estado = EstadoDeSolicitud.EN_ATENCION;
    }

    public void atender() {
        if (this.estado != EstadoDeSolicitud.EN_ATENCION) {
            throw new ExcepcionDeReglaDeDominio("Solo se puede atender una solicitud en atencion");
        }
        this.estado = EstadoDeSolicitud.ATENDIDA;
        this.historial.add(new RegistroHistorial("Solicitud atendida", LocalDateTime.now(), this.estado));
    }

    public void cerrar() {
        if (this.estado != EstadoDeSolicitud.ATENDIDA) {
            throw new ExcepcionDeReglaDeDominio("Solo se puede cerrar una solicitud atendida");
        }
        this.estado = EstadoDeSolicitud.CERRADA;
        this.historial.add(new RegistroHistorial("Solicitud cerrada", LocalDateTime.now(), this.estado));
    }

}
