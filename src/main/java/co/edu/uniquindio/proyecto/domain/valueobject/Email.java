package co.edu.uniquindio.proyecto.domain.valueobject;

public record Email(String valor) {

    public Email {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("El email no puede ser nulo o vacío");
        }
        if (!valor.contains("@")) {
            throw new IllegalArgumentException("El email no tiene el formato correcto");
        }
    }

    @Override
    public String toString() {
        return valor;
    }

}
