package co.edu.uniquindio.proyecto.domain.valueobject;

public record CodigoSolicitud(String valor) {

    public CodigoSolicitud {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("El codigo no puede ser nulo o vacío");
        }
    }

    @Override
    public String toString() {
        return valor;
    }

}
