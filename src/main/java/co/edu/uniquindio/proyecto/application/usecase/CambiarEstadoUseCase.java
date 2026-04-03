package co.edu.uniquindio.proyecto.application.usecase;

import co.edu.uniquindio.proyecto.domain.entity.*;
import co.edu.uniquindio.proyecto.domain.repository.*;
import co.edu.uniquindio.proyecto.domain.valueobject.*;

public class CambiarEstadoUseCase {

    private final SolicitudRepository solicitudRepository;

    public CambiarEstadoUseCase(SolicitudRepository solicitudRepository) {
        this.solicitudRepository = solicitudRepository;
    }

    public void clasificar(CodigoSolicitud codigo, PrioridadDeSolicitud prioridad, String justificacion) {
        Solicitud solicitud = solicitudRepository.obtenerPorCodigo(codigo);
        solicitud.clasificar(prioridad, justificacion);
        solicitudRepository.guardar(solicitud);
    }

    public void atender(CodigoSolicitud codigo) {
        Solicitud solicitud = solicitudRepository.obtenerPorCodigo(codigo);
        solicitud.atender();
        solicitudRepository.guardar(solicitud);
    }
}
