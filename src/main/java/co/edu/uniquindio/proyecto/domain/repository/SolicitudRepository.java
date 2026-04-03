package co.edu.uniquindio.proyecto.domain.repository;

import java.util.List;
import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.*;

public interface SolicitudRepository {
    Solicitud obtenerPorCodigo(CodigoSolicitud codigo);

    void guardar(Solicitud solicitud);

    List<Solicitud> obtenerPorEstado(EstadoDeSolicitud estado);
}
