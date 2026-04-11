package co.edu.uniquindio.proyecto.application.usecase;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.repository.SolicitudRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.CodigoSolicitud;

public class AtenderSolicitudUseCase {

    private final SolicitudRepository solicitudRepository;

    public AtenderSolicitudUseCase(SolicitudRepository solicitudRepository) {
        this.solicitudRepository = solicitudRepository;
    }

    public void ejecutar(String codigo) {
        CodigoSolicitud codigoSolicitud = new CodigoSolicitud(codigo);
        Solicitud solicitud = solicitudRepository.obtenerPorCodigo(codigoSolicitud);
        solicitud.atender();
        solicitudRepository.guardar(solicitud);
    }

}
