package co.edu.uniquindio.proyecto.domain.exception;

import co.edu.uniquindio.proyecto.domain.valueobject.Documento;

public class ExcepcionDeUsuarioNoEncontrado extends RuntimeException {
    public ExcepcionDeUsuarioNoEncontrado(Documento documento) {
        super("No se encontró un usuario con el documento: " + documento.numero());
    }
}