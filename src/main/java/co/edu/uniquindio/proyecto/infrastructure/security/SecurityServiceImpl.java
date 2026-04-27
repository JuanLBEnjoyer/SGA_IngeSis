package co.edu.uniquindio.proyecto.infrastructure.security;

import co.edu.uniquindio.proyecto.infrastructure.security.dto.JwtResponse;
import co.edu.uniquindio.proyecto.infrastructure.security.dto.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public JwtResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        String jwtToken = jwtTokenProvider.generateToken(authentication);

        return JwtResponse.builder()
                .token(jwtToken)
                .build();
    }
}
