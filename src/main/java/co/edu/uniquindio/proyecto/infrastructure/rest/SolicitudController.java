package co.edu.uniquindio.proyecto.infrastructure.rest;

import co.edu.uniquindio.proyecto.application.dto.request.*;
import co.edu.uniquindio.proyecto.application.dto.response.*;
import co.edu.uniquindio.proyecto.application.usecase.*;
import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.*;
import co.edu.uniquindio.proyecto.infrastructure.rest.mapper.SolicitudMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
@RequiredArgsConstructor
@Tag(name = "Solicitudes", description = "Gestión del ciclo de vida completo de una solicitud académica")
public class SolicitudController {

    private final ClasificarSolicitudUseCase clasificarSolicitudUseCase;
    private final AtenderSolicitudUseCase atenderSolicitudUseCase;
    private final AsignarResponsableUseCase asignarResponsableUseCase;
    private final CerrarSolicitudUseCase cerrarSolicitudUseCase;
    private final ConsultarSolicitudesPorEstadoUseCase consultarPorEstadoUseCase;
    private final ObtenerSolicitudUseCase obtenerSolicitudUseCase;
    private final SolicitudMapper mapper;

    // ── GET /api/solicitudes ──────────────────────────────────────────────────
    @GetMapping
    @Operation(summary = "Listar solicitudes", description = "Obtiene la lista de solicitudes. Se puede filtrar por estado.")
    @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    public ResponseEntity<List<SolicitudResumenResponse>> listarSolicitudes(
            @RequestParam(required = false) EstadoDeSolicitud estado) {

        List<Solicitud> solicitudes = consultarPorEstadoUseCase.ejecutar(estado);
        return ResponseEntity.ok(mapper.toResumenResponseList(solicitudes));
    }

    // ── GET /api/solicitudes/{codigo} ─────────────────────────────────────────
    @GetMapping("/{codigo}")
    @Operation(summary = "Obtener detalle de una solicitud", description = "Retorna la información completa de una solicitud por su código.")
    @ApiResponse(responseCode = "200", description = "Solicitud encontrada")
    @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
    public ResponseEntity<SolicitudDetalleResponse> obtenerSolicitud(
            @PathVariable String codigo) {

        Solicitud solicitud = obtenerSolicitudUseCase.ejecutar(codigo);
        return ResponseEntity.ok(mapper.toDetalleResponse(solicitud));
    }

    // ── PUT /api/solicitudes/{codigo}/clasificar ──────────────────────────────
    @PutMapping("/{codigo}/clasificar")
    @Operation(summary = "Clasificar una solicitud", description = "Asigna una prioridad a la solicitud y la pasa a estado CLASIFICADA. "
            + "Precondición: la solicitud debe estar en estado REGISTRADA.")
    @ApiResponse(responseCode = "200", description = "Solicitud clasificada exitosamente")
    @ApiResponse(responseCode = "400", description = "Estado inválido o datos incorrectos")
    @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
    public ResponseEntity<SolicitudDetalleResponse> clasificarSolicitud(
            @PathVariable String codigo,
            @Valid @RequestBody ClasificarSolicitudRequest request) {

        clasificarSolicitudUseCase.ejecutar(codigo, request.prioridad(), request.justificacion());
        Solicitud solicitud = obtenerSolicitudUseCase.ejecutar(codigo);
        return ResponseEntity.ok(mapper.toDetalleResponse(solicitud));
    }

    // ── PUT /api/solicitudes/{codigo}/asignar ─────────────────────────────────
    @PutMapping("/{codigo}/asignar")
    @Operation(summary = "Asignar responsable a una solicitud", description = "Designa un usuario como responsable. "
            + "Precondición: solicitud en estado CLASIFICADA. "
            + "Regla de negocio: el responsable no puede ser ESTUDIANTE.")
    @ApiResponse(responseCode = "200", description = "Responsable asignado exitosamente")
    @ApiResponse(responseCode = "400", description = "Estado inválido o responsable no válido")
    @ApiResponse(responseCode = "404", description = "Solicitud o responsable no encontrado")
    public ResponseEntity<SolicitudDetalleResponse> asignarResponsable(
            @PathVariable String codigo,
            @Valid @RequestBody AsignarResponsableRequest request) {

        asignarResponsableUseCase.ejecutar(codigo, request.documentoResponsable(), request.tipoDocumentoResponsable());
        Solicitud solicitud = obtenerSolicitudUseCase.ejecutar(codigo);
        return ResponseEntity.ok(mapper.toDetalleResponse(solicitud));
    }

    // ── PATCH /api/solicitudes/{codigo}/atender ───────────────────────────────
    @PatchMapping("/{codigo}/atender")
    @Operation(summary = "Marcar una solicitud como atendida", description = "Registra que la solicitud fue atendida. "
            + "Precondición: solicitud en estado EN_ATENCION.")
    @ApiResponse(responseCode = "200", description = "Solicitud marcada como atendida")
    @ApiResponse(responseCode = "400", description = "Estado inválido")
    @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
    public ResponseEntity<SolicitudDetalleResponse> atenderSolicitud(
            @PathVariable String codigo,
            @Valid @RequestBody AtenderSolicitudRequest request) {

        atenderSolicitudUseCase.ejecutar(codigo);
        Solicitud solicitud = obtenerSolicitudUseCase.ejecutar(codigo);
        return ResponseEntity.ok(mapper.toDetalleResponse(solicitud));
    }

    // ── PUT /api/solicitudes/{codigo}/cerrar ──────────────────────────────────
    @PutMapping("/{codigo}/cerrar")
    @Operation(summary = "Cerrar una solicitud", description = "Finaliza el ciclo de vida de la solicitud. "
            + "Precondición: solicitud en estado ATENDIDA.")
    @ApiResponse(responseCode = "200", description = "Solicitud cerrada exitosamente")
    @ApiResponse(responseCode = "400", description = "Estado inválido")
    @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
    public ResponseEntity<SolicitudDetalleResponse> cerrarSolicitud(
            @PathVariable String codigo,
            @Valid @RequestBody CerrarSolicitudRequest request) {

        cerrarSolicitudUseCase.ejecutar(codigo, request.observacion());
        Solicitud solicitud = obtenerSolicitudUseCase.ejecutar(codigo);
        return ResponseEntity.ok(mapper.toDetalleResponse(solicitud));
    }

    // ── GET /api/solicitudes/{codigo}/historial ───────────────────────────────
    @GetMapping("/{codigo}/historial")
    @Operation(summary = "Obtener historial de una solicitud", description = "Retorna todos los registros del historial en orden cronológico.")
    @ApiResponse(responseCode = "200", description = "Historial obtenido exitosamente")
    @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
    public ResponseEntity<List<RegistroHistorialResponse>> obtenerHistorial(
            @PathVariable String codigo) {

        Solicitud solicitud = obtenerSolicitudUseCase.ejecutar(codigo);
        return ResponseEntity.ok(mapper.toHistorialResponseList(solicitud.getHistorial()));
    }
}
