package co.edu.uniquindio.proyecto.infrastructure.security;

import co.edu.uniquindio.proyecto.domain.valueobject.RolUsuario;
import co.edu.uniquindio.proyecto.infrastructure.persistence.entity.UsuarioEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final UsuarioEntity usuarioEntity;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String roleStr = "ROLE_" + usuarioEntity.getRol().name();
        return List.of(new SimpleGrantedAuthority(roleStr));
    }

    @Override
    public String getPassword() {
        return usuarioEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return usuarioEntity.getEmail();
    }

    public String getId() {
        return usuarioEntity.getId() != null ? usuarioEntity.getId().toString() : "";
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true; // Asumiendo que todos están activos por defecto
    }
}
