package co.edu.uniquindio.proyecto.infrastructure.persistence.mapper;

import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.valueobject.Documento;
import co.edu.uniquindio.proyecto.domain.valueobject.Email;
import co.edu.uniquindio.proyecto.domain.valueobject.TipoDeDocumento;
import co.edu.uniquindio.proyecto.infrastructure.persistence.entity.UsuarioEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper de persistencia para Usuario.
 *
 * Domain → Entity: MapStruct genera la implementación automáticamente.
 * Entity → Domain: método default manual porque necesita reconstruir
 * los Value Objects Documento y Email.
 */
@Mapper(componentModel = "spring")
public interface UsuarioPersistenceMapper {

    // ─── Domain → Entity ──────────────────────────────────────────────────────

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "numeroDocumento", source = "documento.numero")
    @Mapping(target = "tipoDocumento", source = "documento.tipo")
    @Mapping(target = "nombre", source = "nombre")
    @Mapping(target = "email", source = "email.valor")
    @Mapping(target = "password", constant = "123456")
    @Mapping(target = "rol", source = "rol")
    UsuarioEntity toEntity(Usuario usuario);

    // ─── Entity → Domain ────────────────

    default Usuario toDomain(UsuarioEntity entity) {
        if (entity == null)
            return null;

        Documento documento = new Documento(
                entity.getNumeroDocumento(),
                TipoDeDocumento.valueOf(entity.getTipoDocumento()));
        Email email = new Email(entity.getEmail());

        return new Usuario(documento, entity.getNombre(), email, entity.getRol());
    }
}
