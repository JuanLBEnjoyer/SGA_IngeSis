package co.edu.uniquindio.proyecto.domain.entity;

import co.edu.uniquindio.proyecto.domain.valueobject.Email;
import lombok.Getter;

@Getter
public class Usuario {

    private String id;
    private String nombre;
    private Email email;

    public Usuario(String id, String nombre, Email email) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
    }
}
