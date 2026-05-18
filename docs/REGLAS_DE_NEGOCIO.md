# Reglas de Negocio

Este documento lista las reglas de negocio del sistema, extraídas de las excepciones de dominio.

---

## Jerarquía de Roles

El sistema define 4 roles con responsabilidades distintas en el ciclo de vida de las solicitudes:

| Rol | Capacidades |
|:----|:------------|
| **ESTUDIANTE** | Crea solicitudes. Consulta sus propias solicitudes. Acepta o rechaza la solución ofrecida. |
| **ADMINISTRATIVO** | Visualiza todas las solicitudes. Clasifica (asigna prioridad) y asigna responsable (DOCENTE o DIRECTIVO). Estas dos acciones deben realizarse juntas. |
| **DOCENTE** | Atiende solicitudes que le fueron asignadas. Solo consulta las solicitudes asignadas a él. |
| **DIRECTIVO** | Atiende solicitudes que le fueron asignadas. Solo consulta las solicitudes asignadas a él. |

---

## Service

### AsignarResponsableService
- Un estudiante no puede ser responsable de una solicitud.
- El responsable asignado debe tener rol `DOCENTE` o `DIRECTIVO`.

### ConsultarSolicitudesPorEstadoUseCase
- Un usuario con rol `ADMINISTRATIVO` puede ver todas las solicitudes del sistema.
- Un usuario con rol `DOCENTE` o `DIRECTIVO` solo puede ver las solicitudes que le fueron asignadas (filtrado por su número de documento).
- Un usuario con rol `ESTUDIANTE` solo puede ver las solicitudes que él mismo creó (filtrado por su documento).

---

## Entity

### Solicitud

**Ciclo de vida:**
```
REGISTRADA → CLASIFICADA → EN_ATENCION → ATENDIDA → CERRADA
                                              ↑          |
                                              └──(rechaza)┘
```

**Reglas de transición de estado:**
- Solo se puede clasificar una solicitud que se encuentre en estado `REGISTRADA`.
- Solo se puede asignar un responsable a una solicitud que se encuentre en estado `CLASIFICADA`.
- Solo se puede atender una solicitud que se encuentre en estado `EN_ATENCION`.
- Solo se puede rechazar la atención de una solicitud que esté en estado `ATENDIDA`.
- Solo se puede cerrar una solicitud que se encuentre en estado `ATENDIDA`.

**Reglas de integridad:**
- El código de la solicitud no puede ser nulo.
- La descripción de la solicitud no puede estar vacía.
- El documento del solicitante no puede estar vacío.
- El tipo de solicitud no puede estar vacío.

**Reglas de observación:**
- Al atender una solicitud se debe registrar una observación obligatoria.
- Al rechazar la atención se debe registrar una justificación obligatoria.
- Al cerrar una solicitud se debe registrar una observación.

### Usuario
- El documento del usuario no puede ser nulo.
- El nombre del usuario no puede estar vacío.
- El email del usuario no puede ser nulo.
- El rol del usuario no puede ser nulo.

---

## Value Objects

### Email
- El email no puede ser nulo o vacío.
- El email debe tener un formato válido (contener `@`).
- El email debe pertenecer a la institución (dominio `uniquindio.edu.co` o `uqvirtual.edu.co`).

### Documento
- El número del documento no puede estar vacío.
- El tipo del documento no puede estar vacío.
- El número de documento no puede contener letras, a menos que sea un pasaporte.

### CodigoSolicitud
- El código de la solicitud no puede ser nulo o vacío.
- El código de la solicitud solo puede contener números.
