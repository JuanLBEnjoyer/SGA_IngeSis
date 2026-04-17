package co.edu.uniquindio.proyecto.application.usecase;

import org.springframework.stereotype.Service;

import co.edu.uniquindio.proyecto.domain.entity.*;
import co.edu.uniquindio.proyecto.domain.repository.*;
import co.edu.uniquindio.proyecto.domain.valueobject.*;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CerrarSolicitudUseCase {

    private final SolicitudRepository solicitudRepository;

    @Transactional
    public void ejecutar(String codigo, String justificacion) {
        CodigoSolicitud codigoSolicitud = new CodigoSolicitud(codigo);
        Solicitud solicitud = solicitudRepository.findById(codigoSolicitud);
        solicitud.cerrar(justificacion);
        solicitudRepository.save(solicitud);
    }
}
