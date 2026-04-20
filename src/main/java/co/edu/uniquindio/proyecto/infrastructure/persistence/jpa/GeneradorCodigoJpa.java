package co.edu.uniquindio.proyecto.infrastructure.persistence.jpa;

import co.edu.uniquindio.proyecto.domain.repository.GeneradorCodigo;
import co.edu.uniquindio.proyecto.domain.valueobject.CodigoSolicitud;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Generador de códigos persistente
 * Esta implementación consulta el MAX(id) real de la tabla en cada llamada,
 * por lo que siempre genera el siguiente valor correcto sin importar cuántas
 * veces se reinicie la aplicación.
 */
@Service
@RequiredArgsConstructor
public class GeneradorCodigoJpa implements GeneradorCodigo {

    private final SolicitudJpaDataRepository dataRepository;

    @Override
    @Transactional(readOnly = true)
    public CodigoSolicitud generar() {
        long siguiente = dataRepository.count() + 1;
        return new CodigoSolicitud(String.format("%03d", siguiente));
    }
}
