package co.edu.uniquindio.proyecto.application.usecase;

import co.edu.uniquindio.proyecto.domain.entity.*;
import co.edu.uniquindio.proyecto.domain.repository.*;
import co.edu.uniquindio.proyecto.domain.service.AsignarResponsableService;
import co.edu.uniquindio.proyecto.domain.valueobject.*;

public class AsignarResponsableUseCase {

    private final SolicitudRepository solicitudRepository;
    private final UsuarioRepository usuarioRepository;
    private final AsignarResponsableService asignarResponsableService;

    public AsignarResponsableUseCase(SolicitudRepository solicitudRepository,
            UsuarioRepository usuarioRepository) {
        this.solicitudRepository = solicitudRepository;
        this.usuarioRepository = usuarioRepository;
        this.asignarResponsableService = new AsignarResponsableService();
    }

    public void ejecutar(CodigoSolicitud codigoSolicitud, Documento documentoResponsable) {
        Solicitud solicitud = solicitudRepository.obtenerPorCodigo(codigoSolicitud);
        Usuario responsable = usuarioRepository.obtenerPorDocumento(documentoResponsable);
        asignarResponsableService.asignar(solicitud, responsable);
        solicitudRepository.guardar(solicitud);
    }
}
