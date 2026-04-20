package co.edu.uniquindio.proyecto.infrastructure.security.config;

import co.edu.uniquindio.proyecto.infrastructure.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class SecurityBeansConfig {

    private final CustomUserDetailsService userDetailsService;

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new AuthenticationProvider() {
            @Override
            public org.springframework.security.core.Authentication authenticate(
                    org.springframework.security.core.Authentication authentication)
                    throws org.springframework.security.core.AuthenticationException {
                String email = authentication.getName();
                String pwd = authentication.getCredentials().toString();
                org.springframework.security.core.userdetails.UserDetails userDetails = userDetailsService
                        .loadUserByUsername(email);
                if (passwordEncoder().matches(pwd, userDetails.getPassword())) {
                    return new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                            userDetails, pwd, userDetails.getAuthorities());
                } else {
                    throw new org.springframework.security.authentication.BadCredentialsException(
                            "Credenciales Inválidas");
                }
            }

            @Override
            public boolean supports(Class<?> authentication) {
                return org.springframework.security.authentication.UsernamePasswordAuthenticationToken.class
                        .isAssignableFrom(authentication);
            }
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
