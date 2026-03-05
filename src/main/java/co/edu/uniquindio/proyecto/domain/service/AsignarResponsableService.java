package co.edu.uniquindio.proyecto.domain.service;

import co.edu.uniquindio.proyecto.domain.entity.*;
import co.edu.uniquindio.proyecto.domain.exception.ExcepcionDeReglaDeDominio;
import co.edu.uniquindio.proyecto.domain.valueobject.RolUsuario;

public class AsignarResponsableService {

    public void asignar(Solicitud solicitud, Usuario responsable) {
        if (responsable.getRol() == RolUsuario.ESTUDIANTE) {
            throw new ExcepcionDeReglaDeDominio("Un estudiante no puede ser responsable");
        }
        solicitud.asignarResponsable(responsable.getDocumento());
    }

}
