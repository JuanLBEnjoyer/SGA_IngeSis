package co.edu.uniquindio.proyecto.application.usecase;

import org.springframework.stereotype.Service;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.repository.SolicitudRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.CodigoSolicitud;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AtenderSolicitudUseCase {

    private final SolicitudRepository solicitudRepository;

    @Transactional
    public void ejecutar(String codigo) {
        CodigoSolicitud codigoSolicitud = new CodigoSolicitud(codigo);
        Solicitud solicitud = solicitudRepository.findById(codigoSolicitud);
        solicitud.atender();
        solicitudRepository.save(solicitud);
    }
}
