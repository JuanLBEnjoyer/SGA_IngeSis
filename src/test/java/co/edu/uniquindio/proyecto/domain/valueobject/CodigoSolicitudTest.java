package co.edu.uniquindio.proyecto.domain.valueobject;

import co.edu.uniquindio.proyecto.domain.exception.ExcepcionDeReglaDeDominio;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CodigoSolicitudTest {


    @Test
    void noDebeCrearCodigoNulo() {
        String codigo = null;
        Exception ex = assertThrows(ExcepcionDeReglaDeDominio.class,
                () -> new CodigoSolicitud(codigo));
        assertEquals("El codigo no puede ser nulo o vacío", ex.getMessage());
    }

    @Test
    void noDebeCrearCodigoVacio() {
        String codigo = " ";
        Exception ex = assertThrows(ExcepcionDeReglaDeDominio.class,
                () -> new CodigoSolicitud(codigo));
        assertEquals("El codigo no puede ser nulo o vacío", ex.getMessage());
    }

    @Test
    void debeCrearCodigoValido() {
        CodigoSolicitud codigo = new CodigoSolicitud("123456");
        assertNotNull(codigo);
    }
}
