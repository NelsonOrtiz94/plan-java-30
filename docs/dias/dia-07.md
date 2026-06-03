# Día 7 — Documentación de API y seguimiento semanal 1
## Concepto base
Una API sin documentación es como una máquina sin manual: puede funcionar, pero otros no sabrán usarla correctamente.
## Temas
- Contratos de API: OpenAPI 3.1.
- SpringDoc: anotaciones @Operation, @ApiResponse, @Schema.
- Versionamiento: URI (/v1/), headers, query params.
- Errores estándar: ProblemDetail (RFC 7807).
- Ejemplos de request/response en la especificación.
## Actividad práctica
- Documentar endpoint POST /api/v1/orders con @Operation.
- Agregar ejemplos de response 201, 400, 422, 500.
- Definir schema de error con ProblemDetail.
- Acceder a Swagger UI: http://localhost:8080/swagger-ui.html
## Seguimiento semanal 1
### Objetivo
Validar comprensión de fundamentos, seguridad, arquitectura hexagonal, pruebas y API.
### Preguntas
- Explique el flujo completo desde el controlador hasta el dominio.
- ¿Qué pasaría si mañana cambiamos la base de datos?
- ¿Qué reglas de negocio no deben estar en el controlador?
- ¿Cómo validaste los errores?
- ¿Qué vulnerabilidades básicas evitaste?
### Señales de alerta
- El caso de uso depende directamente del framework.
- Las reglas de negocio están en el controlador.
- Las pruebas solo validan caminos felices.
- No existe manejo consistente de errores.
- La API no tiene contrato claro.
## Mis notas y decisiones
> Registra aquí tus aprendizajes del día.
