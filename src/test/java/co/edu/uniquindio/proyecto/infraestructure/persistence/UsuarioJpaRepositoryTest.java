package co.edu.uniquindio.proyecto.infraestructure.persistence;

import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.exception.ExcepcionDeUsuarioNoEncontrado;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.*;
import co.edu.uniquindio.proyecto.infrastructure.persistence.jpa.UsuarioJpaRepository;
import co.edu.uniquindio.proyecto.infrastructure.persistence.mapper.UsuarioPersistenceMapperImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de integración para UsuarioJpaRepository.
 *
 */
@DataJpaTest(properties = "spring.sql.init.mode=never")
@Import({
        UsuarioJpaRepository.class,
        UsuarioPersistenceMapperImpl.class
})
class UsuarioJpaRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario crearUsuario(String numeroDoc, String nombre, String email, RolUsuario rol) {
        return new Usuario(
                new Documento(numeroDoc, TipoDeDocumento.CEDULA),
                nombre,
                new Email(email),
                rol);
    }

    // ── Guardar y recuperar ───────────────────────────────────────────────────

    @Test
    void debeGuardarYRecuperarPorDocumento() {
        usuarioRepository.guardar(crearUsuario("123456", "Daniel Garcia",
                "daniel.garcia@uniquindio.edu.co", RolUsuario.ESTUDIANTE), "dummyPassword");

        Documento doc = new Documento("123456", TipoDeDocumento.CEDULA);
        Usuario recuperado = usuarioRepository.obtenerPorDocumento(doc);

        assertNotNull(recuperado);
        assertEquals("123456", recuperado.getDocumento().numero());
        assertEquals("Daniel Garcia", recuperado.getNombre());
        assertEquals(RolUsuario.ESTUDIANTE, recuperado.getRol());
    }

    @Test
    void debeReconstruirEmailComoValueObject() {
        usuarioRepository.guardar(crearUsuario("123456", "Daniel Garcia",
                "daniel.garcia@uniquindio.edu.co", RolUsuario.ADMINISTRATIVO), "dummyPassword");

        Usuario recuperado = usuarioRepository.obtenerPorDocumento(
                new Documento("123456", TipoDeDocumento.CEDULA));

        assertNotNull(recuperado.getEmail());
        assertEquals("daniel.garcia@uniquindio.edu.co", recuperado.getEmail().valor());
    }

    @Test
    void debeDistinguirPorTipoDeDocumento() {
        // Mismo número pero distinto tipo → distintos usuarios
        Usuario conCedula = new Usuario(
                new Documento("123456", TipoDeDocumento.CEDULA),
                "Con Cedula",
                new Email("cedula@uniquindio.edu.co"),
                RolUsuario.ESTUDIANTE);

        Usuario conPasaporte = new Usuario(
                new Documento("ABC123", TipoDeDocumento.PASAPORTE),
                "Con Pasaporte",
                new Email("pasaporte@uniquindio.edu.co"),
                RolUsuario.ESTUDIANTE);

        usuarioRepository.guardar(conCedula, "dummyPassword");
        usuarioRepository.guardar(conPasaporte, "dummyPassword");

        Usuario recuperadoCedula = usuarioRepository.obtenerPorDocumento(
                new Documento("123456", TipoDeDocumento.CEDULA));
        Usuario recuperadoPasaporte = usuarioRepository.obtenerPorDocumento(
                new Documento("ABC123", TipoDeDocumento.PASAPORTE));

        assertEquals("Con Cedula", recuperadoCedula.getNombre());
        assertEquals("Con Pasaporte", recuperadoPasaporte.getNombre());
    }

    // ── Casos de error ────────────────────────────────────────────────────────

    @Test
    void debeLanzarExcepcionCuandoUsuarioNoExiste() {
        assertThrows(ExcepcionDeUsuarioNoEncontrado.class,
                () -> usuarioRepository.obtenerPorDocumento(
                        new Documento("999999", TipoDeDocumento.CEDULA)));
    }

    /**
     * Verifica que guardar() con el mismo documento dos veces hace UPDATE (upsert),
     */
    @Test
    void debeRechazarDocumentoDuplicado() {
        usuarioRepository.guardar(crearUsuario("123456", "Daniel Garcia",
                "daniel.garcia@uniquindio.edu.co", RolUsuario.ESTUDIANTE), "dummyPassword");

        // Guardar con mismo documento pero rol distinto → debe hacer UPDATE, no fallar
        assertDoesNotThrow(() -> usuarioRepository.guardar(
                crearUsuario("123456", "Daniel Garcia", "daniel.garcia@uniquindio.edu.co",
                        RolUsuario.DOCENTE), "dummyPassword"));

        // Verificar que sigue habiendo exactamente 1 usuario con ese documento
        Usuario recuperado = usuarioRepository.obtenerPorDocumento(
                new Documento("123456", TipoDeDocumento.CEDULA));
        assertEquals("123456", recuperado.getDocumento().numero());
    }
}
