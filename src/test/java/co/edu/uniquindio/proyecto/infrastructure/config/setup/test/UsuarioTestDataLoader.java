package co.edu.uniquindio.proyecto.infrastructure.config.setup.test;

import co.edu.uniquindio.proyecto.domain.valueobject.RolUsuario;
import co.edu.uniquindio.proyecto.infrastructure.persistence.entity.UsuarioEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UsuarioTestDataLoader {

    public static final PasswordEncoder encoder = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();

    public static UsuarioEntity usuarioAdminBase() {
        UsuarioEntity admin = new UsuarioEntity();
        admin.setNumeroDocumento("1234567890");
        admin.setTipoDocumento("CEDULA");
        admin.setNombre("Administrador Ficticio");
        admin.setEmail("admin@uniquindio.edu.co");
        admin.setPassword(encoder.encode("adminpass"));
        admin.setRol(RolUsuario.ADMINISTRATIVO);
        return admin;
    }

    public static UsuarioEntity usuarioEstudianteBase() {
        UsuarioEntity user = new UsuarioEntity();
        user.setNumeroDocumento("1094123456");
        user.setTipoDocumento("CEDULA");
        user.setNombre("Estudiante Ficticio");
        user.setEmail("user@uniquindio.edu.co");
        user.setPassword(encoder.encode("userpass"));
        user.setRol(RolUsuario.ESTUDIANTE);
        return user;
    }

    public static UsuarioEntity usuarioDocenteBase() {
        UsuarioEntity docente = new UsuarioEntity();
        docente.setNumeroDocumento("1094123457");
        docente.setTipoDocumento("CEDULA");
        docente.setNombre("Docente Ficticio");
        docente.setEmail("docente@uniquindio.edu.co");
        docente.setPassword(encoder.encode("docentepass"));
        docente.setRol(RolUsuario.DOCENTE);
        return docente;
    }
}
