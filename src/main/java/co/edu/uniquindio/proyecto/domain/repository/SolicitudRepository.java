package co.edu.uniquindio.proyecto.domain.repository;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SolicitudRepository {
    Solicitud obtenerPorCodigo(CodigoSolicitud codigo);

    void guardar(Solicitud solicitud);

    Page<Solicitud> obtenerPorEstado(EstadoDeSolicitud estado, Pageable pageable);

    Page<Solicitud> obtenerExcluyendoEstado(EstadoDeSolicitud estado, Pageable pageable);
}
