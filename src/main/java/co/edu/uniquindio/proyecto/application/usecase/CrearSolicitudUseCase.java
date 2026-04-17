package co.edu.uniquindio.proyecto.application.usecase;

import org.springframework.stereotype.Service;

import co.edu.uniquindio.proyecto.domain.entity.*;
import co.edu.uniquindio.proyecto.domain.repository.*;
import co.edu.uniquindio.proyecto.domain.valueobject.*;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CrearSolicitudUseCase {

    private final SolicitudRepository solicitudRepository;
    private final GeneradorCodigo generadorCodigo;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public Solicitud ejecutar(TipoDeSolicitud tipo, String descripcion, String numeroDocumento,
            TipoDeDocumento tipoDocumento) {

        Documento docSolicitante = new Documento(numeroDocumento, tipoDocumento);
        Usuario solicitante = usuarioRepository.findByDocumento(docSolicitante);

        CodigoSolicitud codigo = generadorCodigo.generar();

        Solicitud solicitud = new Solicitud(codigo, descripcion, solicitante, tipo);
        solicitudRepository.save(solicitud);
        return solicitud;
    }
}