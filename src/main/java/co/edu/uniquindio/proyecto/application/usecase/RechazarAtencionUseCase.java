package co.edu.uniquindio.proyecto.application.usecase;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.repository.SolicitudRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.CodigoSolicitud;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RechazarAtencionUseCase {

    private final SolicitudRepository solicitudRepository;

    @Transactional
    public Solicitud ejecutar(String codigoStr, String justificacion) {
        CodigoSolicitud codigo = new CodigoSolicitud(codigoStr);
        Solicitud solicitud = solicitudRepository.obtenerPorCodigo(codigo);
        solicitud.rechazarAtencion(justificacion);
        solicitudRepository.guardar(solicitud);
        return solicitud;
    }
}
