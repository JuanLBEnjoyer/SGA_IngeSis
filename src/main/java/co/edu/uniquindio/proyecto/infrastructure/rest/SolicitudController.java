package co.edu.uniquindio.proyecto.infrastructure.rest;

import co.edu.uniquindio.proyecto.application.dto.request.*;
import co.edu.uniquindio.proyecto.application.dto.response.*;
import co.edu.uniquindio.proyecto.application.usecase.*;
import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.EstadoDeSolicitud;
import co.edu.uniquindio.proyecto.infrastructure.rest.mapper.SolicitudMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
@RequiredArgsConstructor
@Tag(name = "Solicitudes", description = "Gestión del ciclo de vida completo de una solicitud académica")
public class SolicitudController {

        private final CrearSolicitudUseCase crearSolicitudUseCase;
        private final ClasificarSolicitudUseCase clasificarSolicitudUseCase;
        private final AtenderSolicitudUseCase atenderSolicitudUseCase;
        private final CerrarSolicitudUseCase cerrarSolicitudUseCase;
        private final AsignarResponsableUseCase asignarResponsableUseCase;
        private final ConsultarSolicitudesPorEstadoUseCase consultarPorEstadoUseCase;
        private final ObtenerSolicitudUseCase obtenerSolicitudUseCase;
        private final SolicitudMapper mapper;

        // ── POST /api/solicitudes ─────────────────────────────────────────────────
        @PostMapping
        @Operation(summary = "Registrar una nueva solicitud", description = "Crea una nueva solicitud en estado REGISTRADA. "
                        + "El código se genera automáticamente — no debe enviarse en el body. " +
                        "Mapea a: CrearSolicitudUseCase.")
        @ApiResponse(responseCode = "201", description = "Solicitud registrada exitosamente")
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
        @ApiResponse(responseCode = "404", description = "Solicitante no encontrado")
        public ResponseEntity<SolicitudDetalleResponse> crearSolicitud(
                        @Valid @RequestBody CrearSolicitudRequest request) {

                Solicitud solicitud = crearSolicitudUseCase.ejecutar(
                                request.tipo(),
                                request.descripcion(),
                                request.documentoSolicitante(),
                                request.tipoDocumentoSolicitante());

                SolicitudDetalleResponse response = mapper.toDetalleResponse(solicitud);

                URI location = ServletUriComponentsBuilder
                                .fromCurrentRequest()
                                .path("/{codigo}")
                                .buildAndExpand(solicitud.getCodigo().valor())
                                .toUri();

                return ResponseEntity.created(location).body(response);
        }

        // ── GET /api/solicitudes ──────────────────────────────────────────────────
        @GetMapping
        @Operation(summary = "Listar solicitudes con paginación", description = """
                        Devuelve solicitudes paginadas. Parámetros de query disponibles:
                        - `estado` (opcional): filtra por estado
                        - `page` (por defecto 0): número de página
                        - `size` (por defecto 10): cantidad de elementos por página
                        - `sort` (opcional): campo y dirección, ej: `estado,asc`

                        Ejemplo: `GET /api/solicitudes?estado=REGISTRADA&page=0&size=5`
                        """)
        @ApiResponse(responseCode = "200", description = "Página obtenida exitosamente")
        public ResponseEntity<Page<SolicitudResumenResponse>> listarSolicitudesPaginadas(
                        @RequestParam(required = false) EstadoDeSolicitud estado,
                        @PageableDefault(size = 10, sort = "codigo") Pageable pageable) {

                Page<Solicitud> pagina = consultarPorEstadoUseCase.ejecutar(estado, pageable);
                return ResponseEntity.ok(pagina.map(mapper::toResumenResponse));
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
        @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('DOCENTE', 'ADMINISTRATIVO', 'DIRECTIVO')")
        @Operation(summary = "Clasificar una solicitud", description = "Asigna una prioridad a la solicitud y la pasa a estado CLASIFICADA. "
                        +
                        "Precondición: la solicitud debe estar en estado REGISTRADA.")
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
        @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMINISTRATIVO', 'DIRECTIVO')")
        @Operation(summary = "Asignar responsable a una solicitud", description = "Designa un usuario como responsable. "
                        +
                        "Precondición: solicitud en estado CLASIFICADA. " +
                        "Regla de negocio: el responsable no puede ser ESTUDIANTE.")
        @ApiResponse(responseCode = "200", description = "Responsable asignado exitosamente")
        @ApiResponse(responseCode = "400", description = "Estado inválido o responsable no válido")
        @ApiResponse(responseCode = "404", description = "Solicitud o responsable no encontrado")
        public ResponseEntity<SolicitudDetalleResponse> asignarResponsable(
                        @PathVariable String codigo,
                        @Valid @RequestBody AsignarResponsableRequest request) {

                asignarResponsableUseCase.ejecutar(
                                codigo,
                                request.documentoResponsable(),
                                request.tipoDocumentoResponsable());
                Solicitud solicitud = obtenerSolicitudUseCase.ejecutar(codigo);
                return ResponseEntity.ok(mapper.toDetalleResponse(solicitud));
        }

        // ── PATCH /api/solicitudes/{codigo}/atender ───────────────────────────────
        @PatchMapping("/{codigo}/atender")
        @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('DOCENTE', 'ADMINISTRATIVO', 'DIRECTIVO')")
        @Operation(summary = "Marcar una solicitud como atendida", description = "Registra que la solicitud fue atendida. "
                        +
                        "Precondición: solicitud en estado EN_ATENCION.")
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
        @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('DIRECTIVO')")
        @Operation(summary = "Cerrar una solicitud", description = "Finaliza el ciclo de vida de la solicitud. " +
                        "Precondición: solicitud en estado ATENDIDA.")
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
