# Proyecto

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
- **Autenticación (JWT):** Gestión de roles (ESTUDIANTE, DOCENTE, ADMINISTRATIVO, DIRECTIVO).
- **Gestión de Solicitudes:**
  - Registro de nuevas solicitudes.
  - Clasificación por prioridad con justificación.
  - Asignación a responsables.
  - Atención, cierre e historial completo de cambios de estado.
- **Consultas con Paginación:** Listado de solicitudes utilizando parámetros `page`, `size` y `sort`.
- **Integridad de Datos:** Comprobación transaccional y patrón *Upsert* implementado en la persistencia.

## Documentación de la API (Swagger UI)
La documentación completa e interactiva de la API (estándar OpenAPI) está disponible al ejecutar el proyecto, accediendo a:
- [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- JSON de la API: [http://localhost:8080/api-docs](http://localhost:8080/api-docs)

> **Importante:** La mayoría de los *endpoints* están asegurados. Para interactuar con ellos, primero se debe realizar el login en `/api/auth/login` para recibir un token JWT y configurarlo en la plataforma de Swagger.
