package co.edu.uniquindio.proyecto.infraestructure.rest;

import co.edu.uniquindio.proyecto.application.dto.response.UsuarioDetalleResponse;
import co.edu.uniquindio.proyecto.application.usecase.ObtenerUsuarioPorEmailUseCase;
import co.edu.uniquindio.proyecto.application.usecase.RegistrarUsuarioUseCase;
import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.valueobject.Documento;
import co.edu.uniquindio.proyecto.domain.valueobject.Email;
import co.edu.uniquindio.proyecto.domain.valueobject.RolUsuario;
import co.edu.uniquindio.proyecto.domain.valueobject.TipoDeDocumento;
import co.edu.uniquindio.proyecto.infrastructure.rest.UsuarioController;
import co.edu.uniquindio.proyecto.infrastructure.rest.mapper.UsuarioMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private RegistrarUsuarioUseCase registrarUsuarioUseCase;

        @MockitoBean
        private ObtenerUsuarioPorEmailUseCase obtenerUsuarioPorEmailUseCase;

        @MockitoBean
        private UsuarioMapper mapper;

        @MockitoBean
        private JwtDecoder jwtDecoder;

        @Test
        void debeRegistrarUsuarioExitosamente() throws Exception {
                String requestJson = """
                                {
                                  "numeroDocumento": "123456789",
                                  "tipoDocumento": "CEDULA",
                                  "nombre": "Juan Perez",
                                  "email": "juan@uqvirtual.edu.co",
                                  "password": "Password123!",
                                  "rol": "ESTUDIANTE"
                                }
                                """;

                doNothing().when(registrarUsuarioUseCase).ejecutar(any(), any(), any(), any(), any(), any());

                mockMvc.perform(post("/api/usuarios")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson))
                                .andExpect(status().isCreated());

                verify(registrarUsuarioUseCase, times(1)).ejecutar(
                                "123456789",
                                TipoDeDocumento.CEDULA,
                                "Juan Perez",
                                "juan@uqvirtual.edu.co",
                                "Password123!",
                                RolUsuario.ESTUDIANTE);
        }

        @Test
        void debeRetornar400CuandoFaltanDatosDeRegistro() throws Exception {
                String requestJson = """
                                {
                                  "numeroDocumento": "",
                                  "tipoDocumento": "CEDULA",
                                  "nombre": "Juan Perez",
                                  "email": "juan@uqvirtual.edu.co",
                                  "password": "Password123!",
                                  "rol": "ESTUDIANTE"
                                }
                                """;

                mockMvc.perform(post("/api/usuarios")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.errores.numeroDocumento").exists());

                verifyNoInteractions(registrarUsuarioUseCase);
        }

        @Test
        @WithMockUser(username = "juan@uqvirtual.edu.co", roles = "ESTUDIANTE")
        void debeObtenerPerfilDeUsuarioAutenticado() throws Exception {
                Usuario mockUsuario = new Usuario(
                                new Documento("123456789", TipoDeDocumento.CEDULA),
                                "Juan Perez",
                                new Email("juan@uqvirtual.edu.co"),
                                RolUsuario.ESTUDIANTE);

                UsuarioDetalleResponse responseMock = new UsuarioDetalleResponse(
                                "123456789",
                                "CEDULA",
                                "Juan Perez",
                                "juan@uqvirtual.edu.co",
                                "ESTUDIANTE");

                when(obtenerUsuarioPorEmailUseCase.ejecutar("juan@uqvirtual.edu.co")).thenReturn(mockUsuario);
                when(mapper.toUsuarioDetalle(mockUsuario)).thenReturn(responseMock);

                mockMvc.perform(get("/api/usuarios/me")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.numeroDocumento").value("123456789"))
                                .andExpect(jsonPath("$.nombre").value("Juan Perez"));

                verify(obtenerUsuarioPorEmailUseCase, times(1)).ejecutar("juan@uqvirtual.edu.co");
        }

}
