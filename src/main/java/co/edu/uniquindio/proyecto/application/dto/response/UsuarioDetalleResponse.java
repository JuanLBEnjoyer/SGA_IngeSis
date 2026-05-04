package co.edu.uniquindio.proyecto.application.dto.response;

public record UsuarioDetalleResponse(
        String numeroDocumento,
        String tipoDocumento,
        String nombre,
        String email,
        String rol) {
}
