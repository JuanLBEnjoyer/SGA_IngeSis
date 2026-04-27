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
            String encodedPassword = passwordEncoder.encode("123456");

            UsuarioEntity estudiante = new UsuarioEntity();
            estudiante.setNumeroDocumento("1094123456");
            estudiante.setTipoDocumento("CEDULA");
            estudiante.setNombre("Juan Pablo Galeano");
            estudiante.setEmail("jpgaleano@uqvirtual.edu.co");
            estudiante.setPassword(encodedPassword);
            estudiante.setRol(RolUsuario.ESTUDIANTE);
            usuarioRepository.save(estudiante);

            UsuarioEntity admin = new UsuarioEntity();
            admin.setNumeroDocumento("987654321");
            admin.setTipoDocumento("CEDULA");
            admin.setNombre("Daniel Garcia");
            admin.setEmail("daniel.garcia@uniquindio.edu.co");
            admin.setPassword(encodedPassword);
            admin.setRol(RolUsuario.ADMINISTRATIVO);
            usuarioRepository.save(admin);

            UsuarioEntity docente = new UsuarioEntity();
            docente.setNumeroDocumento("111222333");
            docente.setTipoDocumento("CEDULA");
            docente.setNombre("Maria Lopez");
            docente.setEmail("maria.lopez@uniquindio.edu.co");
            docente.setPassword(encodedPassword);
            docente.setRol(RolUsuario.DOCENTE);
            usuarioRepository.save(docente);

            UsuarioEntity directivo = new UsuarioEntity();
            directivo.setNumeroDocumento("444555666");
            directivo.setTipoDocumento("CEDULA");
            directivo.setNombre("Carlos Ramirez");
            directivo.setEmail("carlos.ramirez@uniquindio.edu.co");
            directivo.setPassword(encodedPassword);
            directivo.setRol(RolUsuario.DIRECTIVO);
            usuarioRepository.save(directivo);

            System.out.println("====== USUARIOS DEFAULT CREADOS EN H2 (BCRYPT) ======");
            System.out.println("Estudiante -> jpgaleano@uqvirtual.edu.co / 123456");
            System.out.println("Administrativo -> daniel.garcia@uniquindio.edu.co / 123456");
            System.out.println("Docente -> maria.lopez@uniquindio.edu.co / 123456");
            System.out.println("Directivo -> carlos.ramirez@uniquindio.edu.co / 123456");
        }
    }
}
