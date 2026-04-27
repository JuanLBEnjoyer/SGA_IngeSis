package co.edu.uniquindio.proyecto.infraestructure.rest;

import co.edu.uniquindio.proyecto.application.dto.response.*;
import co.edu.uniquindio.proyecto.application.usecase.*;
import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.exception.ExcepcionDeSolicitudNoEncontrada;
import co.edu.uniquindio.proyecto.domain.exception.ExcepcionDeUsuarioNoEncontrado;
import co.edu.uniquindio.proyecto.domain.valueobject.*;
import co.edu.uniquindio.proyecto.infrastructure.rest.mapper.SolicitudMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import co.edu.uniquindio.proyecto.infrastructure.rest.SolicitudController;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SolicitudController.class)
@WithMockUser
class SolicitudControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private CrearSolicitudUseCase crearSolicitudUseCase;
        @MockitoBean
        private ClasificarSolicitudUseCase clasificarSolicitudUseCase;
        @MockitoBean
        private AtenderSolicitudUseCase atenderSolicitudUseCase;
        @MockitoBean
        private CerrarSolicitudUseCase cerrarSolicitudUseCase;
        @MockitoBean
        private AsignarResponsableUseCase asignarResponsableUseCase;
        @MockitoBean
        private ConsultarSolicitudesPorEstadoUseCase consultarPorEstadoUseCase;
        @MockitoBean
        private ObtenerSolicitudUseCase obtenerSolicitudUseCase;
        @MockitoBean
        private SolicitudMapper mapper;
        @MockitoBean
        private JwtDecoder jwtDecoder;
        // ── Fixtures ──────────────────────────────────────────────────────────────
        private SolicitudDetalleResponse detalleResponseMock() {
                return new SolicitudDetalleResponse(
                                "001",
                                TipoDeSolicitud.REGISTRAR_ASIGNATURA,
                                "Necesito registrar Programación Avanzada",
                                EstadoDeSolicitud.REGISTRADA,
                                null,
                                new UsuarioResumenResponse("123456", "CEDULA", "Juan Perez",
                                                "juan@uqvirtual.edu.co", "ESTUDIANTE"),
                                null,
                                LocalDateTime.now(),
                                1);
        }

        private Solicitud solicitudMockConCodigo() {
                Solicitud mock = mock(Solicitud.class);
                when(mock.getCodigo()).thenReturn(new CodigoSolicitud("001"));
                return mock;
        }

        // ── POST /api/solicitudes ─────────────────────────────────────────────────
        @Test
        void debeCrearSolicitudYRetornar201() throws Exception {
                Solicitud solicitudMock = solicitudMockConCodigo();
                when(crearSolicitudUseCase.ejecutar(
                                eq(TipoDeSolicitud.REGISTRAR_ASIGNATURA),
                                eq("Necesito registrar Programación Avanzada"),
                                eq("1094123456"),
                                eq(TipoDeDocumento.CEDULA)))
                                .thenReturn(solicitudMock);
                when(mapper.toDetalleResponse(solicitudMock)).thenReturn(detalleResponseMock());

                String body = """
                                {
                                  "tipo": "REGISTRAR_ASIGNATURA",
                                  "descripcion": "Necesito registrar Programación Avanzada",
                                  "documentoSolicitante": "1094123456",
                                  "tipoDocumentoSolicitante": "CEDULA"
                                }
                                """;

                mockMvc.perform(post("/api/solicitudes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                                .andExpect(status().isCreated())
                                .andExpect(header().string("Location",
                                                org.hamcrest.Matchers.endsWith("/api/solicitudes/001")))
                                .andExpect(jsonPath("$.codigo").value("001"))
                                .andExpect(jsonPath("$.estado").value("REGISTRADA"));
        }

        @Test
        void debeRetornar400CuandoDescripcionEsDemasiadoCorta() throws Exception {
                String body = """
                                {
                                  "tipo": "REGISTRAR_ASIGNATURA",
                                  "descripcion": "Corta",
                                  "documentoSolicitante": "1094123456",
                                  "tipoDocumentoSolicitante": "CEDULA"
                                }
                                """;

                mockMvc.perform(post("/api/solicitudes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.errores.descripcion").exists());

                verifyNoInteractions(crearSolicitudUseCase);
        }

        @Test
        void debeRetornar400CuandoFaltaTipoDocumentoSolicitante() throws Exception {
                String body = """
                                {
                                  "tipo": "REGISTRAR_ASIGNATURA",
                                  "descripcion": "Necesito registrar Programación Avanzada",
                                  "documentoSolicitante": "1094123456"
                                }
                                """;

                mockMvc.perform(post("/api/solicitudes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.errores.tipoDocumentoSolicitante").exists());

                verifyNoInteractions(crearSolicitudUseCase);
        }

        @Test
        void debeRetornar404CuandoSolicitanteNoExiste() throws Exception {
                when(crearSolicitudUseCase.ejecutar(any(), any(), any(), any()))
                                .thenThrow(new ExcepcionDeUsuarioNoEncontrado(
                                                new Documento("9999", TipoDeDocumento.CEDULA)));

                String body = """
                                {
                                  "tipo": "REGISTRAR_ASIGNATURA",
                                  "descripcion": "Necesito registrar Programación Avanzada",
                                  "documentoSolicitante": "9999",
                                  "tipoDocumentoSolicitante": "CEDULA"
                                }
                                """;

                mockMvc.perform(post("/api/solicitudes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.status").value(404));
        }

        // ── GET /api/solicitudes ──────────────────────────────────────────
        @Test
        void debeRetornarListaVaciaCuandoNoHaySolicitudes() throws Exception {
                when(consultarPorEstadoUseCase.ejecutar(isNull(), any(Pageable.class))).thenReturn(Page.empty());

                mockMvc.perform(get("/api/solicitudes"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content").isArray());
        }

        @Test
        void debeRetornarListaFiltradaPorEstado() throws Exception {
                when(consultarPorEstadoUseCase.ejecutar(eq(EstadoDeSolicitud.REGISTRADA), any(Pageable.class)))
                                .thenReturn(Page.empty());

                mockMvc.perform(get("/api/solicitudes")
                                .param("estado", "REGISTRADA"))
                                .andExpect(status().isOk());

                verify(consultarPorEstadoUseCase).ejecutar(eq(EstadoDeSolicitud.REGISTRADA), any(Pageable.class));
        }

        // ── GET /api/solicitudes/{codigo} ─────────────────────────────────────────
        @Test
        void debeRetornarDetalleAlObtenerSolicitudExistente() throws Exception {
                Solicitud solicitudMock = mock(Solicitud.class);
                when(obtenerSolicitudUseCase.ejecutar("001")).thenReturn(solicitudMock);
                when(mapper.toDetalleResponse(solicitudMock)).thenReturn(detalleResponseMock());

                mockMvc.perform(get("/api/solicitudes/001"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.codigo").value("001"))
                                .andExpect(jsonPath("$.estado").value("REGISTRADA"));
        }

        @Test
        void debeRetornar404CuandoSolicitudNoExiste() throws Exception {
                when(obtenerSolicitudUseCase.ejecutar("999"))
                                .thenThrow(new ExcepcionDeSolicitudNoEncontrada(new CodigoSolicitud("999")));

                mockMvc.perform(get("/api/solicitudes/999"))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.status").value(404));
        }

        // ── PUT /api/solicitudes/{codigo}/clasificar ──────────────────────────────
        @Test
        @WithMockUser(roles = "ADMINISTRATIVO")
        void debeClasificarSolicitudConDatosValidos() throws Exception {
                Solicitud solicitudMock = mock(Solicitud.class);
                when(obtenerSolicitudUseCase.ejecutar("001")).thenReturn(solicitudMock);
                when(mapper.toDetalleResponse(solicitudMock)).thenReturn(detalleResponseMock());

                String body = """
                                {
                                  "prioridad": "ALTO",
                                  "justificacion": "Inicio de semestre urgente"
                                }
                                """;

                mockMvc.perform(put("/api/solicitudes/001/clasificar")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.codigo").value("001"));

                verify(clasificarSolicitudUseCase).ejecutar(
                                eq("001"),
                                eq(PrioridadDeSolicitud.ALTO),
                                eq("Inicio de semestre urgente"));
        }

        @Test
        @WithMockUser(roles = "ADMINISTRATIVO")
        void debeRetornar400CuandoJustificacionEstaVaciaAlClasificar() throws Exception {
                String body = """
                                {
                                  "prioridad": "MEDIO",
                                  "justificacion": ""
                                }
                                """;

                mockMvc.perform(put("/api/solicitudes/001/clasificar")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.errores.justificacion").exists());

                verifyNoInteractions(clasificarSolicitudUseCase);
        }

        // ── PUT /api/solicitudes/{codigo}/asignar ─────────────────────────────────
        @Test
        @WithMockUser(roles = "ADMINISTRATIVO")
        void debeAsignarResponsableConDocumentoCompleto() throws Exception {
                Solicitud solicitudMock = mock(Solicitud.class);
                when(obtenerSolicitudUseCase.ejecutar("001")).thenReturn(solicitudMock);
                when(mapper.toDetalleResponse(solicitudMock)).thenReturn(detalleResponseMock());

                String body = """
                                {
                                  "documentoResponsable": "987654321",
                                  "tipoDocumentoResponsable": "CEDULA"
                                }
                                """;

                mockMvc.perform(put("/api/solicitudes/001/asignar")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                                .andExpect(status().isOk());

                verify(asignarResponsableUseCase).ejecutar(
                                eq("001"),
                                eq("987654321"),
                                eq(TipoDeDocumento.CEDULA));
        }

        @Test
        @WithMockUser(roles = "ADMINISTRATIVO")
        void debeRetornar400CuandoFaltaTipoDocumentoAlAsignar() throws Exception {
                String body = """
                                {
                                  "documentoResponsable": "987654321"
                                }
                                """;

                mockMvc.perform(put("/api/solicitudes/001/asignar")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.errores.tipoDocumentoResponsable").exists());

                verifyNoInteractions(asignarResponsableUseCase);
        }

        // ── PATCH /api/solicitudes/{codigo}/atender ───────────────────────────────
        @Test
        @WithMockUser(roles = "DOCENTE")
        void debeAtenderSolicitudConObservacionValida() throws Exception {
                Solicitud solicitudMock = mock(Solicitud.class);
                when(obtenerSolicitudUseCase.ejecutar("001")).thenReturn(solicitudMock);
                when(mapper.toDetalleResponse(solicitudMock)).thenReturn(detalleResponseMock());

                String body = """
                                {
                                  "observacion": "Solicitud procesada exitosamente"
                                }
                                """;

                mockMvc.perform(patch("/api/solicitudes/001/atender")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                                .andExpect(status().isOk());

                verify(atenderSolicitudUseCase).ejecutar(eq("001"));
        }

        @Test
        @WithMockUser(roles = "DOCENTE")
        void debeRetornar400CuandoObservacionEstaVaciaAlAtender() throws Exception {
                String body = """
                                {
                                  "observacion": ""
                                }
                                """;

                mockMvc.perform(patch("/api/solicitudes/001/atender")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.errores.observacion").exists());

                verifyNoInteractions(atenderSolicitudUseCase);
        }

        // ── PUT /api/solicitudes/{codigo}/cerrar ──────────────────────────────────
        @Test
        @WithMockUser(roles = "DIRECTIVO")
        void debeCerrarSolicitudConObservacionValida() throws Exception {
                Solicitud solicitudMock = mock(Solicitud.class);
                when(obtenerSolicitudUseCase.ejecutar("001")).thenReturn(solicitudMock);
                when(mapper.toDetalleResponse(solicitudMock)).thenReturn(detalleResponseMock());

                String body = """
                                {
                                  "observacion": "Solicitud resuelta satisfactoriamente"
                                }
                                """;

                mockMvc.perform(put("/api/solicitudes/001/cerrar")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                                .andExpect(status().isOk());

                verify(cerrarSolicitudUseCase).ejecutar(
                                eq("001"),
                                eq("Solicitud resuelta satisfactoriamente"));
        }

        @Test
        @WithMockUser(roles = "DIRECTIVO")
        void debeRetornar400CuandoObservacionEstaVaciaAlCerrar() throws Exception {
                String body = """
                                {
                                  "observacion": ""
                                }
                                """;

                mockMvc.perform(put("/api/solicitudes/001/cerrar")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.errores.observacion").exists());

                verifyNoInteractions(cerrarSolicitudUseCase);
        }

        // ── GET /api/solicitudes/{codigo}/historial ───────────────────────────────
        @Test
        void debeRetornarHistorialDeSolicitud() throws Exception {
                Solicitud solicitudMock = mock(Solicitud.class);
                when(solicitudMock.getHistorial()).thenReturn(List.of());
                when(obtenerSolicitudUseCase.ejecutar("001")).thenReturn(solicitudMock);
                when(mapper.toHistorialResponseList(List.of())).thenReturn(List.of());

                mockMvc.perform(get("/api/solicitudes/001/historial"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$").isArray());
        }
}