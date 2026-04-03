package co.edu.uniquindio.proyecto.domain.exception;

import co.edu.uniquindio.proyecto.domain.valueobject.CodigoSolicitud;

public class ExcepcionDeSolicitudNoEncontrada extends RuntimeException {
    public ExcepcionDeSolicitudNoEncontrada(CodigoSolicitud codigo) {
        super("No se encontró una solicitud con el código: " + codigo.valor());
    }
}