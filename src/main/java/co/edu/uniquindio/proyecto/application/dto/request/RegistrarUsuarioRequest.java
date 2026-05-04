package co.edu.uniquindio.proyecto.application.dto.request;

import co.edu.uniquindio.proyecto.domain.valueobject.RolUsuario;
import co.edu.uniquindio.proyecto.domain.valueobject.TipoDeDocumento;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO para registrar un nuevo usuario en el sistema.
 * Mapea a: RegistrarUsuarioUseCase
 *
 * La contraseña se encripta con BCrypt antes de persistirse.
 * El email debe pertenecer al dominio institucional.
 */
public record RegistrarUsuarioRequest(

        @NotBlank(message = "El número de documento es obligatorio") String numeroDocumento,

        @NotNull(message = "El tipo de documento es obligatorio") TipoDeDocumento tipoDocumento,

        @NotBlank(message = "El nombre es obligatorio") @Size(min = 2, max = 150, message = "El nombre debe tener entre 2 y 150 caracteres") String nombre,

        @NotBlank(message = "El email es obligatorio") @Email(message = "El email no tiene formato válido") String email,

        @NotBlank(message = "La contraseña es obligatoria") @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres") String password,

        @NotNull(message = "El rol es obligatorio") RolUsuario rol

) {
}
