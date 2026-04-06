package co.edu.uniquindio.proyecto.application.usecase;

import co.edu.uniquindio.proyecto.domain.entity.*;
import co.edu.uniquindio.proyecto.domain.repository.*;
import co.edu.uniquindio.proyecto.domain.valueobject.*;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CerrarSolicitudUseCase {

    private final SolicitudRepository solicitudRepository;

    public void ejecutar(CodigoSolicitud codigo, String justificacion) {
        Solicitud solicitud = solicitudRepository.obtenerPorCodigo(codigo);
        solicitud.cerrar(justificacion);
        solicitudRepository.guardar(solicitud);
    }
}
