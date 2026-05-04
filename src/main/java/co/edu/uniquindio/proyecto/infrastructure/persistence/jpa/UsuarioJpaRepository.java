package co.edu.uniquindio.proyecto.infrastructure.persistence.jpa;

import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.exception.ExcepcionDeUsuarioNoEncontrado;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.Documento;
import co.edu.uniquindio.proyecto.infrastructure.persistence.mapper.UsuarioPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * ADAPTADOR: Implementa UsuarioRepository del dominio usando H2 + Spring Data
 * JPA.
 */
@Repository
@Transactional
@RequiredArgsConstructor
public class UsuarioJpaRepository implements UsuarioRepository {

        private final UsuarioJpaDataRepository dataRepository;
        private final UsuarioPersistenceMapper mapper;

        @Override
        @Transactional(readOnly = true)
        public Usuario obtenerPorDocumento(Documento documento) {
                return dataRepository
                                .findByNumeroDocumentoAndTipoDocumento(
                                                documento.numero(),
                                                documento.tipo().name())
                                .map(mapper::toDomain)
                                .orElseThrow(() -> new ExcepcionDeUsuarioNoEncontrado(documento));
        }

        @Override
        public void guardar(Usuario usuario, String passwordEncriptado) {
                var entity = mapper.toEntity(usuario);
                
                if (passwordEncriptado != null) {
                        entity.setPassword(passwordEncriptado);
                }
                
                // Upsert: si ya existe un usuario con ese documento, reusar su ID
                // para que JPA haga UPDATE en lugar de INSERT (evita violar UNIQUE).
                dataRepository.findByNumeroDocumentoAndTipoDocumento(
                                usuario.getDocumento().numero(),
                                usuario.getDocumento().tipo().name()).ifPresent(existing -> {
                                        entity.setId(existing.getId());
                                        if (passwordEncriptado == null) {
                                                entity.setPassword(existing.getPassword());
                                        }
                                });
                dataRepository.save(entity);
        }
}