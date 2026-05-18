# Glosario de Lenguaje Ubicuo

Este documento define los conceptos clave utilizados en el dominio del proyecto.

| Concepto                 | Definición                                                                                                                                              |
|:-------------------------|:--------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Solicitud**            | Entidad principal que representa una petición realizada por un Estudiante. Contiene código, descripción, estado, prioridad, responsable e historial.   |
| **Usuario**              | Entidad que representa a una persona que interactúa con el sistema. Se identifica por su documento y tiene un rol que determina sus capacidades.        |
| **Documento**            | Objeto de valor que representa el documento de identidad de un usuario (número + tipo).                                                                 |
| **CodigoSolicitud**      | Identificador único de una solicitud, generado automáticamente por el sistema.                                                                          |
| **TipoDeSolicitud**      | Clasificación del tipo de solicitud realizada (REGISTRAR_ASIGNATURA, HOMOLOGACION, CANCELACION_ASIGNATURA, SOLICITUD_CUPO, CONSULTA_ACADEMICA).         |
| **EstadoDeSolicitud**    | Estado actual en el ciclo de vida de una solicitud (REGISTRADA, CLASIFICADA, EN_ATENCION, ATENDIDA, CERRADA).                                           |
| **PrioridadDeSolicitud** | Nivel de urgencia asignado a una solicitud por el Administrativo (BAJO, MEDIO, ALTO, URGENTE).                                                          |
| **RegistroHistorial**    | Registro de un evento o cambio de estado en la historia de una solicitud. Incluye descripción, fecha y estado asociado.                                 |
| **Clasificar**           | Acción exclusiva del Administrativo: asigna una prioridad a una solicitud y la pasa a estado CLASIFICADA.                                               |
| **Asignar Responsable**  | Acción exclusiva del Administrativo: designa un Docente o Directivo para atender la solicitud y la pasa a estado EN_ATENCION.                           |
| **Atender**              | Acción del Docente o Directivo asignado: registra la solución ofrecida con una observación obligatoria y pasa la solicitud a estado ATENDIDA.           |
| **Aceptar Solución**     | Acción del Estudiante: indica su conformidad con la solución, cerrando definitivamente la solicitud (estado CERRADA).                                   |
| **Rechazar Atención**    | Acción del Estudiante: indica que la solución no es satisfactoria con una justificación obligatoria; la solicitud regresa al estado EN_ATENCION.         |
| **Cerrar**               | Acción del Estudiante al aceptar la solución ofrecida. Finaliza el ciclo de vida de la solicitud (estado CERRADA).                                      |
| **RolUsuario**           | Rol que desempeña un usuario en el sistema: ESTUDIANTE, DOCENTE, ADMINISTRATIVO o DIRECTIVO.                                                            |
| **Email**                | Dirección de correo electrónico institucional de un usuario (dominio uniquindio.edu.co o uqvirtual.edu.co).                                             |
| **Responsable**          | Usuario con rol DOCENTE o DIRECTIVO designado por el Administrativo para atender una solicitud específica.                                              |
