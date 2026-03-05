package co.edu.uniquindio.proyecto.domain.valueobject;

import co.edu.uniquindio.proyecto.domain.exception.ExcepcionDeReglaDeDominio;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EmailTest {

    @Test
    void dosEmailsConMismoValorDebenSerIguales() {
        Email email1 = new Email("usuario@uniquindio.edu.co");
        Email email2 = new Email("usuario@uniquindio.edu.co");
        assertEquals(email1, email2);
        assertEquals(email1.hashCode(), email2.hashCode());
    }

    @Test
    void noDebeCrearEmailInvalido() {
        String email = "correo-invalido";
        Exception ex = assertThrows(ExcepcionDeReglaDeDominio.class,
                () -> new Email(email));
        assertEquals("El email no tiene el formato correcto", ex.getMessage());
    }

    @Test
    void noDebeCrearEmailVacio() {
        String email = " ";
        Exception ex = assertThrows(ExcepcionDeReglaDeDominio.class,
                () -> new Email(email));
        assertEquals("El email no puede ser nulo o vacío", ex.getMessage());
    }

    @Test
    void noDebeCrearEmailNulo() {
        String email = null;
        Exception ex = assertThrows(ExcepcionDeReglaDeDominio.class,
                () -> new Email(email));
        assertEquals("El email no puede ser nulo o vacío", ex.getMessage());
    }

    @Test
    void debeCrearEmailInstitcional() {
        Email emailInstitcional = new Email("usuario@uniquindio.edu.co");
        assertNotNull(emailInstitcional);
    }

    @Test
    void debeCrearEmailInstitcional2() {
        Email emailInstitcional = new Email("usuario@uqvirtual.edu.co");
        assertNotNull(emailInstitcional);
    }

    @Test
    void noDebePermitirDominioNoInstitucional() {
        String email = "usuario@gmail.com";
        Exception ex = assertThrows(ExcepcionDeReglaDeDominio.class,
                () -> new Email(email));
        assertEquals("El email no pertenece a la institucion", ex.getMessage());
    }

    @Test
    void noDebePermitirDominioNoInstitucional2() {
        String email = "usuario@hotmail.com";
        Exception ex = assertThrows(ExcepcionDeReglaDeDominio.class,
                () -> new Email(email));
        assertEquals("El email no pertenece a la institucion", ex.getMessage());
    }

    @Test
    void noDebePermitirDominioNoInstitucional3() {
        String email = "usuario@uniquindio.com";
        Exception ex = assertThrows(ExcepcionDeReglaDeDominio.class,
                () -> new Email(email));
        assertEquals("El email no pertenece a la institucion", ex.getMessage());
    }

    @Test
    void noDebePermitirDominioNoInstitucional4() {
        String email = "usuario@uqvirtual.com";
        Exception ex = assertThrows(ExcepcionDeReglaDeDominio.class,
                () -> new Email(email));
        assertEquals("El email no pertenece a la institucion", ex.getMessage());
    }
}
