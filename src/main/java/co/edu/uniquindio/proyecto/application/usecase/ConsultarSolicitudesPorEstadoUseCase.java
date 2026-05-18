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
    public Page<Solicitud> ejecutar(EstadoDeSolicitud estado, TipoDeSolicitud tipo,
            PrioridadDeSolicitud prioridad, String documentoResponsable,
            Pageable pageable, String email, String rolUsuario) {

        Usuario usuario = usuarioRepository.obtenerPorEmail(email);

        if (rolUsuario.contains("ADMINISTRATIVO")) {
            return solicitudRepository.obtenerPorFiltros(estado, tipo, prioridad, documentoResponsable, pageable);
        } else if (rolUsuario.contains("DOCENTE") || rolUsuario.contains("DIRECTIVO")) {
            return solicitudRepository.obtenerPorFiltros(estado, tipo, prioridad, usuario.getDocumento().numero(),
                    pageable);
        } else {
            return solicitudRepository.obtenerPorFiltrosYSolicitante(estado, tipo, prioridad, documentoResponsable,
                    usuario.getDocumento().numero(), pageable);
        }
    }
}
