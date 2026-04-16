package co.edu.uniquindio.proyecto.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.edu.uniquindio.proyecto.application.usecase.*;
import co.edu.uniquindio.proyecto.domain.repository.*;
import co.edu.uniquindio.proyecto.domain.service.AsignarResponsableService;

@Configuration
public class UseCaseConfig {

    @Bean
    public AsignarResponsableService asignarResponsableService() {
        return new AsignarResponsableService();
    }

    @Bean
    public CrearSolicitudUseCase crearSolicitudUseCase(
            SolicitudRepository solicitudRepository,
            GeneradorCodigo generadorCodigo,
            UsuarioRepository usuarioRepository) {
        return new CrearSolicitudUseCase(solicitudRepository, generadorCodigo, usuarioRepository);
    }

    @Bean
    public ClasificarSolicitudUseCase clasificarSolicitudUseCase(
            SolicitudRepository solicitudRepository) {
        return new ClasificarSolicitudUseCase(solicitudRepository);
    }

    @Bean
    public AtenderSolicitudUseCase atenderSolicitudUseCase(
            SolicitudRepository solicitudRepository) {
        return new AtenderSolicitudUseCase(solicitudRepository);
    }

    @Bean
    public CerrarSolicitudUseCase cerrarSolicitudUseCase(
            SolicitudRepository solicitudRepository) {
        return new CerrarSolicitudUseCase(solicitudRepository);
    }

    @Bean
    public AsignarResponsableUseCase asignarResponsableUseCase(
            SolicitudRepository solicitudRepository,
            UsuarioRepository usuarioRepository,
            AsignarResponsableService asignarResponsableService) {
        return new AsignarResponsableUseCase(solicitudRepository, usuarioRepository, asignarResponsableService);
    }

    @Bean
    public ConsultarSolicitudesPorEstadoUseCase consultarSolicitudesPorEstadoUseCase(
            SolicitudRepository solicitudRepository) {
        return new ConsultarSolicitudesPorEstadoUseCase(solicitudRepository);
    }

    @Bean
    public ObtenerSolicitudUseCase obtenerSolicitudUseCase(
            SolicitudRepository solicitudRepository) {
        return new ObtenerSolicitudUseCase(solicitudRepository);
    }
}
