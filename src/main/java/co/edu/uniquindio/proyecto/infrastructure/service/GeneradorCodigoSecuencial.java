package co.edu.uniquindio.proyecto.infrastructure.service;

import co.edu.uniquindio.proyecto.domain.repository.GeneradorCodigo;
import co.edu.uniquindio.proyecto.domain.valueobject.CodigoSolicitud;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class GeneradorCodigoSecuencial implements GeneradorCodigo {

    private final AtomicInteger contador = new AtomicInteger(1);

    @Override
    public CodigoSolicitud generar() {
        int numero = contador.getAndIncrement();
        String valor = String.format("%03d", numero);
        return new CodigoSolicitud(valor);
    }
}
