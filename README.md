# Sistema de Gestion Academica

## Descripción
Sistema para la gestión de solicitudes académicas del Programa de Ingenieria de Sistemas y Computacion

> **Nota:** Este proyecto se encuentra actualmente en fase de desarrollo y sufrirá diferentes cambios a lo largo del curso de Programación Avanzada.

## Integrantes
1. Juan Pablo Galeano Correa
2. Daniel Estiven Garcia Jaramillo

## Tecnologías Utilizadas
- **Lenguaje**: Java 25
- **Framework Core**: Spring Boot 4.0.2
- **Persistencia**: Spring Data JPA + H2 Database (En memoria)
- **Seguridad**: Spring Security + JWT (JSON Web Tokens)
- **Validación**: Spring Validation (Bean Validation)
- **Documentación API**: Springdoc OpenAPI / Swagger UI
- **Herramientas Adicionales**: Gradle, Lombok, MapStruct
- **Testing**: JUnit 5, Mockito, Spring Boot Test

## Arquitectura
El proyecto sigue una arquitectura limpia basada en el Diseño Guiado por el Dominio (DDD), separando las capas de dominio, aplicación e infraestructura.

## Funcionalidades Principales
- **Gestión de Usuarios:**
  - Registro público de nuevos usuarios con cifrado de contraseña (BCrypt).
  - Consulta del perfil de usuario autenticado.
- **Autenticación (JWT):** Gestión de seguridad robusta usando OAuth2 Resource Server con validación de roles (ESTUDIANTE, DOCENTE, ADMINISTRATIVO, DIRECTIVO).
- **Gestión de Solicitudes:**
  - Registro de nuevas solicitudes.
  - Clasificación por prioridad con justificación.
  - Asignación a responsables.
  - Atención, cierre e historial completo de cambios de estado.
- **Consultas con Paginación:** Listado de solicitudes utilizando parámetros `page`, `size` y `sort`.
- **Integridad de Datos:** Comprobación transaccional y patrón *Upsert* implementado en la persistencia.

## Arquitectura de Pruebas
El proyecto cuenta con una cobertura de pruebas exhaustiva que sigue estrictos estándares académicos y de la industria:
- **Pruebas de Repositorio (`@DataJpaTest`):** Verificación directa contra H2 en memoria con aislamiento total entre pruebas (`spring.sql.init.mode=never` y esquemas `create-drop`).
- **Pruebas Unitarias de Negocio:** Validación de la lógica en los Casos de Uso mediante *Mockito*.
- **Pruebas de Controladores (`@WebMvcTest`):** Verificación de las capas de infraestructura REST con usuarios simulados (`@WithMockUser`).
- **Pruebas End-to-End (`@SpringBootTest`):** Pruebas de integración total que levantan el contexto de Spring, siembran la BD mediante fábricas (`TestDataLoaders`), ejecutan peticiones de *login real* extrayendo el JWT y prueban los endpoints securizados simulando un entorno de producción.

## Documentación de la API (Swagger UI)
La documentación completa e interactiva de la API (estándar OpenAPI) está disponible al ejecutar el proyecto, accediendo a:
- [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- JSON de la API: [http://localhost:8080/api-docs](http://localhost:8080/api-docs)

> **Importante:** La mayoría de los *endpoints* están asegurados. Para interactuar con ellos, primero se debe realizar el login en `/api/auth/login` para recibir un token JWT y configurarlo en la plataforma de Swagger. Además, el contrato completo de la API se encuentra en `docs/api/openapi.yaml`.
