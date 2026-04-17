package co.edu.uniquindio.proyecto.application.usecase;

import org.springframework.stereotype.Service;

import co.edu.uniquindio.proyecto.domain.entity.*;
import co.edu.uniquindio.proyecto.domain.repository.*;
import co.edu.uniquindio.proyecto.domain.service.AsignarResponsableService;
import co.edu.uniquindio.proyecto.domain.valueobject.*;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AsignarResponsableUseCase {

    private final SolicitudRepository solicitudRepository;
    private final UsuarioRepository usuarioRepository;
    private final AsignarResponsableService asignarResponsableService;

    @Transactional
    public void ejecutar(String codigo, String numeroDocumento, TipoDeDocumento tipoDocumento) {
        CodigoSolicitud codigoSolicitud = new CodigoSolicitud(codigo);
        Solicitud solicitud = solicitudRepository.findById(codigoSolicitud);
        Documento documentoResponsable = new Documento(numeroDocumento, tipoDocumento);
        Usuario responsable = usuarioRepository.findByDocumento(documentoResponsable);
        asignarResponsableService.asignar(solicitud, responsable);
        solicitudRepository.save(solicitud);
    }
}
