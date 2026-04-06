package co.edu.uniquindio.proyecto.application.usecase;

import co.edu.uniquindio.proyecto.domain.entity.*;
import co.edu.uniquindio.proyecto.domain.repository.*;
import co.edu.uniquindio.proyecto.domain.valueobject.*;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CrearSolicitudUseCase {

    private final SolicitudRepository solicitudRepository;
    private final GeneradorCodigo generadorCodigo;

    public Solicitud ejecutar(TipoDeSolicitud tipo, String descripcion, Usuario solicitante) {
        CodigoSolicitud codigo = generadorCodigo.generar();
        Solicitud solicitud = new Solicitud(codigo, descripcion, solicitante, tipo);
        solicitudRepository.guardar(solicitud);
        return solicitud;
    }
}