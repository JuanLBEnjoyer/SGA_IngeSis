package co.edu.uniquindio.proyecto.domain.valueobject;

import co.edu.uniquindio.proyecto.domain.exception.ExcepcionDeReglaDeDominio;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EmailTest {

    @Test
    void dosEmailsConMismoValorDebenSerIguales() {
        Email e1 = new Email("usuario@uniquindio.edu.co");
        Email e2 = new Email("usuario@uniquindio.edu.co");
        assertEquals(e1, e2);
        assertEquals(e1.hashCode(), e2.hashCode());
    }

    @Test
    void noDebeCrearEmailInvalido() {
        String emailInvalido = "correo-invalido";
        Exception ex = assertThrows(ExcepcionDeReglaDeDominio.class,
                () -> new Email(emailInvalido));
        assertEquals("El email no tiene el formato correcto", ex.getMessage());
    }
}
//
//Patrón AAA (Arrange, Act, Assert)
//Todas nuestras pruebas seguirán este patrón:
// 1. Arrange (Preparar): Crear el contexto inicial
// configurando los objetos necesarios y el estado
//inicial.
//2. Act (Actuar): Ejecutar el comportamiento que
// queremos probar (metodo).
//3. Assert (Comprobar): Verificar que el resultado
// y el estado final son los esperados (incluyendo
//                                                                                                       excepciones si se viola una regla).