package co.edu.uniquindio.proyecto.application.usecase;

import co.edu.uniquindio.proyecto.domain.entity.*;
import co.edu.uniquindio.proyecto.domain.repository.*;
import co.edu.uniquindio.proyecto.domain.service.AsignarResponsableService;
import co.edu.uniquindio.proyecto.domain.valueobject.*;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AsignarResponsableUseCase {

    private final SolicitudRepository solicitudRepository;
    private final UsuarioRepository usuarioRepository;
    private final AsignarResponsableService asignarResponsableService;

    public void ejecutar(String codigo, String numeroDocumento, TipoDeDocumento tipoDocumento) {
        CodigoSolicitud codigoSolicitud = new CodigoSolicitud(codigo);
        Solicitud solicitud = solicitudRepository.obtenerPorCodigo(codigoSolicitud);
        Documento documentoResponsable = new Documento(numeroDocumento, tipoDocumento);
        Usuario responsable = usuarioRepository.obtenerPorDocumento(documentoResponsable);
        asignarResponsableService.asignar(solicitud, responsable);
        solicitudRepository.guardar(solicitud);
    }
}
