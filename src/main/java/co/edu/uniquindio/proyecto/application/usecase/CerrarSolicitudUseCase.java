package co.edu.uniquindio.proyecto.application.usecase;

import co.edu.uniquindio.proyecto.domain.entity.*;
import co.edu.uniquindio.proyecto.domain.repository.*;
import co.edu.uniquindio.proyecto.domain.valueobject.*;

public class CerrarSolicitudUseCase {

    private final SolicitudRepository solicitudRepository;

    public CerrarSolicitudUseCase(SolicitudRepository solicitudRepository) {
        this.solicitudRepository = solicitudRepository;
    }

    public void ejecutar(CodigoSolicitud codigo) {
        Solicitud solicitud = solicitudRepository.obtenerPorCodigo(codigo);
        solicitud.cerrar();
        solicitudRepository.guardar(solicitud);
    }
}
