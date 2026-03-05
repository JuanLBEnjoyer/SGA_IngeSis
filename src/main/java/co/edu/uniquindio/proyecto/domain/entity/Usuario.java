package co.edu.uniquindio.proyecto.domain.entity;

import co.edu.uniquindio.proyecto.domain.exception.ExcepcionDeReglaDeDominio;
import co.edu.uniquindio.proyecto.domain.valueobject.*;

import lombok.Getter;

@Getter
public class Usuario {

    private Documento documento;
    private String nombre;
    private Email email;
    private RolUsuario rol;

    public Usuario(Documento documento, String nombre, Email email, RolUsuario rol) {
        if (documento == null) {
            throw new ExcepcionDeReglaDeDominio("El documento no puede ser nulo");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new ExcepcionDeReglaDeDominio("El nombre no puede estar vacio");
        }
        if (email == null) {
            throw new ExcepcionDeReglaDeDominio("El email no puede ser nulo");
        }
        if (rol == null) {
            throw new ExcepcionDeReglaDeDominio("El rol no puede ser nulo");
        }
        this.documento = documento;
        this.nombre = nombre;
        this.email = email;
        this.rol = rol;
    }
}
