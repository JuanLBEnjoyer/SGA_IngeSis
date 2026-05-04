package co.edu.uniquindio.proyecto.infrastructure.rest.mapper;

import co.edu.uniquindio.proyecto.application.dto.response.UsuarioDetalleResponse;
import co.edu.uniquindio.proyecto.application.dto.response.UsuarioResumenResponse;
import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(target = "numeroDocumento", source = "documento.numero")
    @Mapping(target = "tipoDocumento", source = "documento.tipo")
    @Mapping(target = "nombre", source = "nombre")
    @Mapping(target = "email", source = "email.valor")
    @Mapping(target = "rol", source = "rol")
    UsuarioResumenResponse toUsuarioResumen(Usuario usuario);

    @Mapping(target = "numeroDocumento", source = "documento.numero")
    @Mapping(target = "tipoDocumento", source = "documento.tipo")
    @Mapping(target = "nombre", source = "nombre")
    @Mapping(target = "email", source = "email.valor")
    @Mapping(target = "rol", source = "rol")
    UsuarioDetalleResponse toUsuarioDetalle(Usuario usuario);
}
