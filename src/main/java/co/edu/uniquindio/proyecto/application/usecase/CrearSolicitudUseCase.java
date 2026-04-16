package co.edu.uniquindio.proyecto.application.usecase;


import co.edu.uniquindio.proyecto.domain.entity.*;
import co.edu.uniquindio.proyecto.domain.repository.*;
import co.edu.uniquindio.proyecto.domain.valueobject.*;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CrearSolicitudUseCase {

    private final SolicitudRepository solicitudRepository;
    private final GeneradorCodigo generadorCodigo;
    private final UsuarioRepository usuarioRepository;

    public Solicitud ejecutar(TipoDeSolicitud tipo, String descripcion, String numeroDocumento,
            TipoDeDocumento tipoDocumento) {

        Documento docSolicitante = new Documento(numeroDocumento, tipoDocumento);
        Usuario solicitante = usuarioRepository.obtenerPorDocumento(docSolicitante);

        CodigoSolicitud codigo = generadorCodigo.generar();

        Solicitud solicitud = new Solicitud(codigo, descripcion, solicitante, tipo);
        solicitudRepository.guardar(solicitud);
        return solicitud;
    }
}