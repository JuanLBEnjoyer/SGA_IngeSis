package co.edu.uniquindio.proyecto.application.usecase;

import co.edu.uniquindio.proyecto.domain.entity.*;
import co.edu.uniquindio.proyecto.domain.repository.*;
import co.edu.uniquindio.proyecto.domain.valueobject.*;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClasificarSolicitudUseCase {

    private final SolicitudRepository solicitudRepository;

    public void ejecutar(String codigo, PrioridadDeSolicitud prioridad, String justificacion) {
        CodigoSolicitud codigoSolicitud = new CodigoSolicitud(codigo);
        Solicitud solicitud = solicitudRepository.obtenerPorCodigo(codigoSolicitud);
        solicitud.clasificar(prioridad, justificacion);
        solicitudRepository.guardar(solicitud);
    }

}
