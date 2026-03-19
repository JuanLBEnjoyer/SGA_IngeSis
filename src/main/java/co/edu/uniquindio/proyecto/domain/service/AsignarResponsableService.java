package co.edu.uniquindio.proyecto.domain.service;

import co.edu.uniquindio.proyecto.domain.entity.*;
import co.edu.uniquindio.proyecto.domain.exception.ExcepcionDeReglaDeDominio;
import co.edu.uniquindio.proyecto.domain.valueobject.RolUsuario;

public class AsignarResponsableService {

    public void asignar(Solicitud solicitud, Usuario responsable) {
        if (solicitud == null) {
            throw new ExcepcionDeReglaDeDominio("La solicitud no puede ser nula");
        }
        if (responsable == null) {
            throw new ExcepcionDeReglaDeDominio("El responsable no puede ser nulo");
        }

        if (responsable.getRol() == RolUsuario.ESTUDIANTE) {
            throw new ExcepcionDeReglaDeDominio("Un estudiante no puede ser responsable");
        }
        solicitud.asignarResponsable(responsable);
    }

}
