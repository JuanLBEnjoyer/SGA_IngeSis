package co.edu.uniquindio.proyecto.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import co.edu.uniquindio.proyecto.domain.service.AsignarResponsableService;

@Configuration
public class UseCaseConfig {
    @Bean
    public AsignarResponsableService asignarResponsableService() {
        return new AsignarResponsableService();
    }
}
