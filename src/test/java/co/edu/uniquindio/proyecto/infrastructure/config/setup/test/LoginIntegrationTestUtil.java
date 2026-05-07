package co.edu.uniquindio.proyecto.infrastructure.config.setup.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LoginIntegrationTestUtil {

    public static String obtenerTokenIntegro(String correo, String contrasenaPlana, MockMvc mvc, ObjectMapper objMapper) throws Exception {
        String req = String.format("{\"email\":\"%s\",\"password\":\"%s\"}", correo, contrasenaPlana);

        var result = mvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content(req))
                .andExpect(status().isOk())
                .andReturn();

        var bodyResponseJson = result.getResponse().getContentAsString();
        return JsonPath.parse(bodyResponseJson).read("$.token").toString();
    }
}
