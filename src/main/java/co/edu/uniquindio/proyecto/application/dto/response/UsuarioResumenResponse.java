package co.edu.uniquindio.proyecto.application.dto.response;

/**
 * DTO auxiliar con información resumida de un Usuario.
 * Se usa dentro de otros Response DTOs (no se expone solo).
 *
 * No exponemos la entidad Usuario completa porque:
 * - No es necesario mostrar todos sus campos en cada respuesta
 * - Evitamos acoplar la API a la estructura interna del dominio
 */
public record UsuarioResumenResponse(
        String numeroDocumento,
        String tipoDocumento,
        String nombre,
        String email,
        String rol) {
}
