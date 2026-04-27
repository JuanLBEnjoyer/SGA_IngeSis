package co.edu.uniquindio.proyecto.infrastructure.security;

import co.edu.uniquindio.proyecto.infrastructure.security.dto.JwtResponse;
import co.edu.uniquindio.proyecto.infrastructure.security.dto.LoginRequest;

public interface SecurityService {
    JwtResponse login(LoginRequest request);
}
