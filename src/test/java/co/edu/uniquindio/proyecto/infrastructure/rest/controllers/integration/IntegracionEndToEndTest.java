package co.edu.uniquindio.proyecto.infrastructure.rest.controllers.integration;

import co.edu.uniquindio.proyecto.infrastructure.config.setup.test.LoginIntegrationTestUtil;
import co.edu.uniquindio.proyecto.infrastructure.config.setup.test.UsuarioTestDataLoader;
import co.edu.uniquindio.proyecto.infrastructure.persistence.entity.UsuarioEntity;
import co.edu.uniquindio.proyecto.infrastructure.persistence.jpa.UsuarioJpaDataRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class IntegracionEndToEndTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objMapper = new ObjectMapper();

    @Autowired
    private UsuarioJpaDataRepository repositorioreal;

    private UsuarioEntity semillaGuardada;

    @BeforeEach
    void setupRealDatabase() {
        repositorioreal.deleteAll();
        semillaGuardada = repositorioreal.save(UsuarioTestDataLoader.usuarioAdminBase());
    }

    @Test
    void testTravesiaCompletaDePeticionAEndpointSeguro() throws Exception {
        // 1. Efectuamos el puente real del log-in
        String tokenVivo = LoginIntegrationTestUtil.obtenerTokenIntegro(
                semillaGuardada.getEmail(),
                "adminpass", // Contraseña nativa pre-encriptada
                mockMvc, objMapper
        );

        // 2. Comisionamos Postman simulado atacando endpoints duros (/api/usuarios/me)
        mockMvc.perform(get("/api/usuarios/me")
                        .header("Authorization", "Bearer " + tokenVivo))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("admin@uniquindio.edu.co"));
    }
}
