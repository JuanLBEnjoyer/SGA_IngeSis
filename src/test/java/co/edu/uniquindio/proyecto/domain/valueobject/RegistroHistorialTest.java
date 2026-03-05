package co.edu.uniquindio.proyecto.domain.valueobject;

import co.edu.uniquindio.proyecto.domain.exception.ExcepcionDeReglaDeDominio;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class RegistroHistorialTest {

    @Test
    void noDebeCrearRegistroConDescripcionVacia() {
        Exception ex = assertThrows(ExcepcionDeReglaDeDominio.class,
                () -> new RegistroHistorial(
                        "",
                        LocalDateTime.now(),
                        EstadoDeSolicitud.CLASIFICADA));
        assertEquals("La descripcion no puede estar vacia", ex.getMessage());
    }
    @Test
    void noDebeCrearRegistroConDescripcionNula() {
        Exception ex = assertThrows(ExcepcionDeReglaDeDominio.class,
                () -> new RegistroHistorial(
                        null,
                        LocalDateTime.now(),
                        EstadoDeSolicitud.CLASIFICADA));
        assertEquals("La descripcion no puede estar vacia", ex.getMessage());
    }
    @Test
    void noDebeCrearRegistroConFechaNula() {
        Exception ex = assertThrows(ExcepcionDeReglaDeDominio.class,
                () -> new RegistroHistorial(
                        "Solicitud creada",
                        null,
                        EstadoDeSolicitud.CLASIFICADA));
        assertEquals("La fecha no puede ser nula", ex.getMessage());
    }
    @Test
    void noDebeCrearRegistroConEstadoNulo() {
        Exception ex = assertThrows(ExcepcionDeReglaDeDominio.class,
                () -> new RegistroHistorial(
                        "Solicitud creada",
                        LocalDateTime.now(),
                        null));
        assertEquals("El estado asociado no puede ser nulo", ex.getMessage());
    }
    @Test
    void debeCrearRegistroHistorialValido() {
        RegistroHistorial registro = new RegistroHistorial(
                "Solicitud creada",
                LocalDateTime.now(),
                EstadoDeSolicitud.CLASIFICADA);
        assertNotNull(registro);
    }
}
