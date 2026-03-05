package co.edu.uniquindio.proyecto.domain.valueobject;

import co.edu.uniquindio.proyecto.domain.exception.ExcepcionDeReglaDeDominio;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DocumentoTest {

    @Test
    void dosDocumentosIgualesDebenSerIguales() {
        Documento doc1 = new Documento("123456", TipoDeDocumento.CEDULA);
        Documento doc2 = new Documento("123456", TipoDeDocumento.CEDULA);

        assertEquals(doc1, doc2);
    }
    @Test
    void noDebeCrearDocumentoConNumeroVacio(){
        Exception ex = assertThrows(ExcepcionDeReglaDeDominio.class,
                () -> new Documento("",TipoDeDocumento.CEDULA));
        assertEquals("El numero del documento no puede estar vacio", ex.getMessage());
    }
    @Test
    void noDebeCrearDocumentoConNumeroNulo(){
        Exception ex = assertThrows(ExcepcionDeReglaDeDominio.class,
                () -> new Documento(null,TipoDeDocumento.CEDULA));
        assertEquals("El numero del documento no puede estar vacio", ex.getMessage());
    }
    @Test
    void noDebeCrearDocumentoConTipooNulo(){
        Exception ex = assertThrows(ExcepcionDeReglaDeDominio.class,
                () -> new Documento("123456",null));
        assertEquals("El tipo del documento no puede estar vacio", ex.getMessage());
    }
    @Test
    void noDebeCrearDocumentoConLetras(){
        Exception ex = assertThrows(ExcepcionDeReglaDeDominio.class,
                () -> new Documento("ABC123",TipoDeDocumento.CEDULA));
        assertEquals("El numero de documento no puede tener letras", ex.getMessage());
    }
    @Test
    void debeCrearDocumentoConLetrasSiEsPasaporte(){
        Documento documento = new Documento("ABC123",TipoDeDocumento.PASAPORTE);
        assertNotNull(documento);
    }
    @Test
    void debeCrearDocumentoConNumeroValido() {
        Documento documento = new Documento("123456789", TipoDeDocumento.CEDULA);
        assertNotNull(documento);
    }
}
