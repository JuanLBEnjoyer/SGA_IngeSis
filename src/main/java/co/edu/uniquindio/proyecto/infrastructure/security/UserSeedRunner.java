package co.edu.uniquindio.proyecto.infrastructure.security;

import co.edu.uniquindio.proyecto.domain.valueobject.RolUsuario;
import co.edu.uniquindio.proyecto.infrastructure.persistence.entity.UsuarioEntity;
import co.edu.uniquindio.proyecto.infrastructure.persistence.jpa.UsuarioJpaDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserSeedRunner implements CommandLineRunner {

    private final UsuarioJpaDataRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (usuarioRepository.count() == 0) {
            String encodedPassword = passwordEncoder.encode("admin123");

            UsuarioEntity admin = new UsuarioEntity();
            admin.setNumeroDocumento("1234567890");
            admin.setTipoDocumento("CC");
            admin.setNombre("Administrador Principal");
            admin.setEmail("admin@uniquindio.edu.co");
            admin.setPassword(encodedPassword);
            admin.setRol(RolUsuario.DIRECTIVO); // Será mapeado a ADMIN

            usuarioRepository.save(admin);

            UsuarioEntity estudiante = new UsuarioEntity();
            estudiante.setNumeroDocumento("1094000123");
            estudiante.setTipoDocumento("TI");
            estudiante.setNombre("Estudiante General");
            estudiante.setEmail("estudiante@uqvirtual.edu.co");
            estudiante.setPassword(passwordEncoder.encode("user123"));
            estudiante.setRol(RolUsuario.ESTUDIANTE); // Será mapeado a USER

            usuarioRepository.save(estudiante);

            System.out.println("====== USUARIOS DEFAULT CREADOS EN H2 ======");
            System.out.println("Admin -> admin@uniquindio.edu.co / admin123");
            System.out.println("User -> estudiante@uqvirtual.edu.co / user123");
        }
    }
}
