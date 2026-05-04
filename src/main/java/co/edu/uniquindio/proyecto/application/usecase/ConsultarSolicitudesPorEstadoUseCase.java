package co.edu.uniquindio.proyecto.application.usecase;

import co.edu.uniquindio.proyecto.domain.entity.*;
import co.edu.uniquindio.proyecto.domain.repository.*;
import co.edu.uniquindio.proyecto.domain.valueobject.*;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConsultarSolicitudesPorEstadoUseCase {

    private final SolicitudRepository solicitudRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public Page<Solicitud> ejecutar(EstadoDeSolicitud estado, Pageable pageable, String email, boolean maxJerarquia) {
        if (maxJerarquia) {
            return solicitudRepository.obtenerPorEstado(estado, pageable);
        } else {
            Usuario usuario = usuarioRepository.obtenerPorEmail(email);
            return solicitudRepository.obtenerPorEstadoYSolicitante(estado, usuario.getDocumento().numero(), pageable);
        }
    }
}
