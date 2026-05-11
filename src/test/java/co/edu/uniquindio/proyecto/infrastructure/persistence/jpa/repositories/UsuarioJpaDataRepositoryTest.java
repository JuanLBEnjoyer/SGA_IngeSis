package co.edu.uniquindio.proyecto.infrastructure.persistence.jpa.repositories;

import co.edu.uniquindio.proyecto.domain.valueobject.RolUsuario;
import co.edu.uniquindio.proyecto.infrastructure.persistence.entity.UsuarioEntity;
import co.edu.uniquindio.proyecto.infrastructure.persistence.jpa.UsuarioJpaDataRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import jakarta.persistence.EntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(properties = "spring.sql.init.mode=never")
class UsuarioJpaDataRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UsuarioJpaDataRepository usuarioRepository;

    @Test
    void debeEncontrarUsuarioPorEmail() {
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setNumeroDocumento("123456");
        usuario.setTipoDocumento("CEDULA");
        usuario.setNombre("Test User");
        usuario.setEmail("test@uniquindio.edu.co");
        usuario.setPassword("hash");
        usuario.setRol(RolUsuario.ESTUDIANTE);

        entityManager.persist(usuario);
        entityManager.flush();

        Optional<UsuarioEntity> encontrado = usuarioRepository.findByEmail("test@uniquindio.edu.co");

        assertTrue(encontrado.isPresent());
        assertEquals("Test User", encontrado.get().getNombre());
    }

    @Test
    void debeRetornarVacioSiEmailNoExiste() {
        Optional<UsuarioEntity> encontrado = usuarioRepository.findByEmail("inexistente@uniquindio.edu.co");

        assertFalse(encontrado.isPresent());
    }
}
