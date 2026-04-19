package co.edu.uniquindio.proyecto.infrastructure.persistence.jpa;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.exception.ExcepcionDeSolicitudNoEncontrada;
import co.edu.uniquindio.proyecto.domain.repository.SolicitudRepository;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.CodigoSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.EstadoDeSolicitud;
import co.edu.uniquindio.proyecto.infrastructure.persistence.entity.SolicitudEntity;
import co.edu.uniquindio.proyecto.infrastructure.persistence.mapper.SolicitudPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
@RequiredArgsConstructor
public class SolicitudJpaRepository implements SolicitudRepository {

    private final SolicitudJpaDataRepository dataRepository;
    private final SolicitudPersistenceMapper mapper;
    private final UsuarioRepository usuarioRepository;

    @Override
    public void guardar(Solicitud solicitud) {
        SolicitudEntity entity = mapper.toEntity(solicitud);
        dataRepository.findByCodigo(solicitud.getCodigo().valor())
                .ifPresent(existing -> entity.setId(existing.getId()));
        dataRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Solicitud obtenerPorCodigo(CodigoSolicitud codigo) {
        return dataRepository.findByCodigo(codigo.valor())
                .map(entity -> mapper.toDomain(entity, usuarioRepository))
                .orElseThrow(() -> new ExcepcionDeSolicitudNoEncontrada(codigo));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Solicitud> obtenerPorEstado(EstadoDeSolicitud estado, Pageable pageable) {
        if (estado == null) {
            return dataRepository.findAll(pageable)
                    .map(entity -> mapper.toDomain(entity, usuarioRepository));
        }
        return dataRepository.findByEstado(estado, pageable)
                .map(entity -> mapper.toDomain(entity, usuarioRepository));
    }
}