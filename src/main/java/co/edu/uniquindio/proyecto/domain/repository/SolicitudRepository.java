package co.edu.uniquindio.proyecto.domain.repository;

import java.util.List;
import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.*;

public interface SolicitudRepository {
    Solicitud findById(CodigoSolicitud codigo);

    void save(Solicitud solicitud);

    List<Solicitud> findByEstado(EstadoDeSolicitud estado);
}
