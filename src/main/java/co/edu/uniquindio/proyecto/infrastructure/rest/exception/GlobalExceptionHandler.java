package co.edu.uniquindio.proyecto.infrastructure.rest.exception;

import co.edu.uniquindio.proyecto.domain.exception.ExcepcionDeReglaDeDominio;
import co.edu.uniquindio.proyecto.domain.exception.ExcepcionDeSolicitudNoEncontrada;
import co.edu.uniquindio.proyecto.domain.exception.ExcepcionDeUsuarioNoEncontrado;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ── Errores de validación Bean Validation (@Valid) ────────────────────────
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(
            MethodArgumentNotValidException ex, WebRequest request) {

        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> fieldErrors.put(error.getField(), error.getDefaultMessage()));

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad Request");
        body.put("mensaje", "Error de validación en los datos de entrada");
        body.put("errores", fieldErrors);
        body.put("path", request.getDescription(false).replace("uri=", ""));

        return ResponseEntity.badRequest().body(body);
    }

    // ── Solicitud no encontrada → 404 ─────────────────────────────────────────
    @ExceptionHandler(ExcepcionDeSolicitudNoEncontrada.class)
    public ResponseEntity<Map<String, Object>> handleSolicitudNoEncontrada(
            ExcepcionDeSolicitudNoEncontrada ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    // ── Usuario no encontrado → 404 ───────────────────────────────────────────
    @ExceptionHandler(ExcepcionDeUsuarioNoEncontrado.class)
    public ResponseEntity<Map<String, Object>> handleUsuarioNoEncontrado(
            ExcepcionDeUsuarioNoEncontrado ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    // ── Regla de dominio violada → 400 ────────────────────────────────────────
    @ExceptionHandler(ExcepcionDeReglaDeDominio.class)
    public ResponseEntity<Map<String, Object>> handleReglaDeDominio(
            ExcepcionDeReglaDeDominio ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    // ── Cualquier otra excepción no controlada → 500 ──────────────────────────
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(
            Exception ex, WebRequest request) {
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocurrió un error inesperado. Por favor intente más tarde.",
                request);
    }

    // ── Método auxiliar ───────────────────────────────────────────────────────
    private ResponseEntity<Map<String, Object>> buildErrorResponse(
            HttpStatus status, String mensaje, WebRequest request) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("mensaje", mensaje);
        body.put("path", request.getDescription(false).replace("uri=", ""));

        return ResponseEntity.status(status).body(body);
    }
}
