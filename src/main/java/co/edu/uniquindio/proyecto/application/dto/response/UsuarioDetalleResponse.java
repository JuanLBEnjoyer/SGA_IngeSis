package co.edu.uniquindio.proyecto.application.dto.response;

import co.edu.uniquindio.proyecto.domain.valueobject.RolUsuario;
import co.edu.uniquindio.proyecto.domain.valueobject.TipoDeDocumento;

/**
 * DTO de respuesta con los datos completos de un usuario.
 * Usado en:
 * - POST /api/usuarios (respuesta al registrar)
 * - GET /api/usuarios/me (datos del usuario autenticado)
 */
public record UsuarioDetalleResponse(
        String numeroDocumento,
        TipoDeDocumento tipoDocumento,
        String nombre,
        String email,
        RolUsuario rol) {
}
