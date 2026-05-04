package co.edu.uniquindio.proyecto.infrastructure.rest;

import co.edu.uniquindio.proyecto.application.dto.request.RegistrarUsuarioRequest;
import co.edu.uniquindio.proyecto.application.dto.response.UsuarioDetalleResponse;
import co.edu.uniquindio.proyecto.application.usecase.ObtenerUsuarioPorEmailUseCase;
import co.edu.uniquindio.proyecto.application.usecase.RegistrarUsuarioUseCase;
import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.infrastructure.rest.mapper.UsuarioMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Gestión de usuarios del sistema")
public class UsuarioController {

    private final RegistrarUsuarioUseCase registrarUsuarioUseCase;
    private final ObtenerUsuarioPorEmailUseCase obtenerUsuarioPorEmailUseCase;
    private final UsuarioMapper mapper;

    @PostMapping
    @Operation(summary = "Registrar un nuevo usuario", description = "Crea un nuevo usuario en el sistema. La contraseña se almacena encriptada (BCrypt).")
    @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    public ResponseEntity<Void> registrarUsuario(@Valid @RequestBody RegistrarUsuarioRequest request) {
        registrarUsuarioUseCase.ejecutar(
                request.numeroDocumento(),
                request.tipoDocumento(),
                request.nombre(),
                request.email(),
                request.password(),
                request.rol());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/me")
    @Operation(summary = "Obtener perfil del usuario", description = "Retorna la información del usuario actualmente autenticado.")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado")
    public ResponseEntity<UsuarioDetalleResponse> obtenerPerfil() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Usuario usuario = obtenerUsuarioPorEmailUseCase.ejecutar(email);
        return ResponseEntity.ok(mapper.toUsuarioDetalle(usuario));
    }
}
