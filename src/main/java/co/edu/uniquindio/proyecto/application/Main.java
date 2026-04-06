package co.edu.uniquindio.proyecto.application;

import co.edu.uniquindio.proyecto.application.usecase.*;
import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.repository.GeneradorCodigo;
import co.edu.uniquindio.proyecto.domain.repository.SolicitudRepository;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;
import co.edu.uniquindio.proyecto.domain.service.AsignarResponsableService;
import co.edu.uniquindio.proyecto.domain.valueobject.*;
import co.edu.uniquindio.proyecto.infrastructure.repository.SolicitudRepositoryEnMemoria;
import co.edu.uniquindio.proyecto.infrastructure.repository.UsuarioRepositoryEnMemoria;
import co.edu.uniquindio.proyecto.infrastructure.service.GeneradorCodigoSecuencial;

import java.util.List;

public class Main {

        public static void main(String[] args) {

                // --- Infraestructura ---
                SolicitudRepository solicitudRepo = new SolicitudRepositoryEnMemoria();
                UsuarioRepository usuarioRepo = new UsuarioRepositoryEnMemoria();
                GeneradorCodigo generadorCodigo = new GeneradorCodigoSecuencial();
                AsignarResponsableService asignarResponsableService = new AsignarResponsableService();

                // --- Casos de uso ---
                CrearSolicitudUseCase crearSolicitud = new CrearSolicitudUseCase(solicitudRepo, generadorCodigo);
                AsignarResponsableUseCase asignarResponsable = new AsignarResponsableUseCase(solicitudRepo,
                                usuarioRepo, asignarResponsableService);
                CambiarEstadoUseCase cambiarEstado = new CambiarEstadoUseCase(solicitudRepo);
                CerrarSolicitudUseCase cerrarSolicitud = new CerrarSolicitudUseCase(solicitudRepo);
                ConsultarSolicitudesPorEstadoUseCase consultarPorEstado = new ConsultarSolicitudesPorEstadoUseCase(
                                solicitudRepo);

                // --- Datos de prueba ---
                Documento docEstudiante = new Documento("1094123456", TipoDeDocumento.CEDULA);
                Usuario estudiante = new Usuario(
                                docEstudiante,
                                "Juan Pablo Galeano",
                                new Email("jpgaleano@uqvirtual.edu.co"),
                                RolUsuario.ESTUDIANTE);

                Documento docAdministrativo = new Documento("987654321", TipoDeDocumento.CEDULA);
                Usuario administrativo = new Usuario(
                                docAdministrativo,
                                "Daniel Garcia",
                                new Email("daniel.garcia@uniquindio.edu.co"),
                                RolUsuario.ADMINISTRATIVO);
                usuarioRepo.guardar(administrativo);

                // --- Flujo completo de una solicitud ---
                System.out.println("=== PRUEBA DE CASOS DE USO ===\n");

                // 1. Crear solicitud
                Solicitud solicitud = crearSolicitud.ejecutar(
                                TipoDeSolicitud.REGISTRAR_ASIGNATURA,
                                "Necesito registrar Programación Avanzada",
                                estudiante);
                System.out.println(
                                "Solicitud creada: " + solicitud.getCodigo() + " | Estado: " + solicitud.getEstado());

                // 2. Clasificar (cambiar estado)
                cambiarEstado.clasificar(solicitud.getCodigo(), PrioridadDeSolicitud.ALTO, "Inicio de semestre");
                System.out.println("Solicitud clasificada | Estado: " + solicitud.getEstado() + " | Prioridad: "
                                + solicitud.getPrioridad());

                // 3. Asignar responsable
                asignarResponsable.ejecutar(solicitud.getCodigo(), docAdministrativo);
                System.out.println("Responsable asignado: " + solicitud.getResponsable().getNombre() + " | Estado: "
                                + solicitud.getEstado());

                // 4. Atender
                cambiarEstado.atender(solicitud.getCodigo());
                System.out.println("Solicitud atendida | Estado: " + solicitud.getEstado());

                // 5. Cerrar
                cerrarSolicitud.ejecutar(solicitud.getCodigo(), "Solicitud cerrada");
                System.out.println("Solicitud cerrada | Estado: " + solicitud.getEstado());

                // 6. Crear una segunda solicitud y consultar por estado
                crearSolicitud.ejecutar(
                                TipoDeSolicitud.HOMOLOGACION,
                                "Solicito homologación de Algoritmos",
                                estudiante);

                List<Solicitud> registradas = consultarPorEstado.ejecutar(EstadoDeSolicitud.REGISTRADA);
                System.out.println("\nSolicitudes en estado REGISTRADA: " + registradas.size());

                List<Solicitud> cerradas = consultarPorEstado.ejecutar(EstadoDeSolicitud.CERRADA);
                System.out.println("Solicitudes en estado CERRADA: " + cerradas.size());

                // 7. Historial de la primera solicitud
                System.out.println("\n=== HISTORIAL DE LA SOLICITUD " + solicitud.getCodigo() + " ===");
                solicitud.getHistorial().forEach(
                                r -> System.out.println("  [" + r.estadoAsociado() + "] " + r.descripcion() + " - "
                                                + r.fecha()));
        }
}
