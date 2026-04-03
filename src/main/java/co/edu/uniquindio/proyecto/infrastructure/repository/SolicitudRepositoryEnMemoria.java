package co.edu.uniquindio.proyecto.infrastructure.repository;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.exception.ExcepcionDeSolicitudNoEncontrada;
import co.edu.uniquindio.proyecto.domain.repository.SolicitudRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.CodigoSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.EstadoDeSolicitud;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SolicitudRepositoryEnMemoria implements SolicitudRepository {

    private final Map<CodigoSolicitud, Solicitud> solicitudes = new HashMap<>();

    @Override
    public Solicitud obtenerPorCodigo(CodigoSolicitud codigo) {
        Solicitud solicitud = solicitudes.get(codigo);
        if (solicitud == null) {
            throw new ExcepcionDeSolicitudNoEncontrada(codigo);
        }
        return solicitud;
    }

    @Override
    public void guardar(Solicitud solicitud) {
        solicitudes.put(solicitud.getCodigo(), solicitud);
    }

    @Override
    public List<Solicitud> obtenerPorEstado(EstadoDeSolicitud estado) {
        return solicitudes.values().stream()
                .filter(s -> s.getEstado() == estado)
                .toList();
    }
}
