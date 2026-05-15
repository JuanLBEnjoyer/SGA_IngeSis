package co.edu.uniquindio.proyecto.application.services.unit;

import co.edu.uniquindio.proyecto.application.usecase.ConsultarSolicitudesPorEstadoUseCase;
import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.repository.SolicitudRepository;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.Documento;
import co.edu.uniquindio.proyecto.domain.valueobject.EstadoDeSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.TipoDeDocumento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultarSolicitudesPorEstadoUseCaseTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ConsultarSolicitudesPorEstadoUseCase useCase;

    private Pageable pageable;
    private Page<Solicitud> dummyPage;
    private Usuario dummyUsuario;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10);
        dummyPage = new PageImpl<>(List.of(mock(Solicitud.class)));
        dummyUsuario = mock(Usuario.class);
    }

    @Test
    void debeConsultarTodasLasSolicitudesSiMaxJerarquiaEsVerdadero() {
        when(solicitudRepository.obtenerPorFiltros(EstadoDeSolicitud.REGISTRADA, null, null, null, pageable))
                .thenReturn(dummyPage);

        Page<Solicitud> result = useCase.ejecutar(EstadoDeSolicitud.REGISTRADA, null, null, null, pageable, "admin@uniquindio.edu.co", true);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(solicitudRepository, times(1)).obtenerPorFiltros(EstadoDeSolicitud.REGISTRADA, null, null, null, pageable);
        verify(usuarioRepository, never()).obtenerPorEmail(anyString());
        verify(solicitudRepository, never()).obtenerPorEstadoYSolicitante(any(), anyString(), any());
    }

    @Test
    void debeConsultarSoloSolicitudesDelUsuarioSiMaxJerarquiaEsFalso() {
        Documento doc = new Documento("12345", TipoDeDocumento.CEDULA);
        when(dummyUsuario.getDocumento()).thenReturn(doc);
        when(usuarioRepository.obtenerPorEmail("estudiante@uniquindio.edu.co")).thenReturn(dummyUsuario);
        when(solicitudRepository.obtenerPorEstadoYSolicitante(EstadoDeSolicitud.REGISTRADA, "12345", pageable))
                .thenReturn(dummyPage);

        Page<Solicitud> result = useCase.ejecutar(EstadoDeSolicitud.REGISTRADA, null, null, null, pageable, "estudiante@uniquindio.edu.co", false);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(usuarioRepository, times(1)).obtenerPorEmail("estudiante@uniquindio.edu.co");
        verify(solicitudRepository, times(1)).obtenerPorEstadoYSolicitante(EstadoDeSolicitud.REGISTRADA, "12345", pageable);
        verify(solicitudRepository, never()).obtenerPorEstado(any(), any());
    }
}
