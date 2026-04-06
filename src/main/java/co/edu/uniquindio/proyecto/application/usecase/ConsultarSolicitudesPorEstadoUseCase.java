package co.edu.uniquindio.proyecto.application.usecase;

import co.edu.uniquindio.proyecto.domain.entity.*;
import co.edu.uniquindio.proyecto.domain.repository.*;
import co.edu.uniquindio.proyecto.domain.valueobject.*;

import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ConsultarSolicitudesPorEstadoUseCase {

    private final SolicitudRepository solicitudRepository;

    public List<Solicitud> ejecutar(EstadoDeSolicitud estado) {
        return solicitudRepository.obtenerPorEstado(estado);
    }
}
