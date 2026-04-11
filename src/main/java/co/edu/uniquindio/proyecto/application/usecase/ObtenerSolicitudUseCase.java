package co.edu.uniquindio.proyecto.application.usecase;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.repository.SolicitudRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.CodigoSolicitud;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ObtenerSolicitudUseCase {

    private final SolicitudRepository solicitudRepository;

    public Solicitud ejecutar(String codigo) {
        CodigoSolicitud codigoSolicitud = new CodigoSolicitud(codigo);
        return solicitudRepository.obtenerPorCodigo(codigoSolicitud);
    }

}
