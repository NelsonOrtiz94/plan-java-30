# Día 1 — Fundamentos web, API y comunicación cliente-servidor
## Concepto base
Una aplicación distribuida funciona como una conversación entre actores. El cliente solicita, el servidor interpreta, procesa y responde. HTTP es el idioma; HTTPS agrega confidencialidad e integridad.
## Temas
- Cliente-servidor: roles, responsabilidades, separación de capas.
- HTTP y HTTPS: diferencias, TLS, certificados.
- Métodos HTTP: GET, POST, PUT, PATCH, DELETE, HEAD, OPTIONS.
- Códigos de estado: 1xx, 2xx, 3xx, 4xx, 5xx.
- API como contrato: semántica, versionamiento, estabilidad.
- Tokens, autenticación y autorización: JWT, OAuth2.
- Principios SOLID aplicados al backend.
- Diferencia entre API, servicio, recurso y endpoint.
## Actividad práctica
### Contrato inicial de la API de órdenes
| Método | Endpoint                              | Descripción               | Éxito | Errores       |
|--------|---------------------------------------|---------------------------|-------|---------------|
| POST   | /api/v1/orders                        | Crear una orden           | 201   | 400, 422, 500 |
| GET    | /api/v1/orders/{orderId}              | Consultar orden por ID    | 200   | 404, 500      |
| GET    | /api/v1/orders?customerId={id}        | Listar órdenes de cliente | 200   | 400, 404, 500 |
| PATCH  | /api/v1/orders/{orderId}/cancel       | Cancelar una orden        | 200   | 404, 409, 500 |
### Request — Crear orden
```json
POST /api/v1/orders
{
  "customerId": "cust-001",
  "items": [
    { "productId": "prod-001", "quantity": 2, "unitPrice": 15000 },
    { "productId": "prod-002", "quantity": 1, "unitPrice": 45000 }
  ]
}
```
### Response — Orden creada (201)
```json
{
  "orderId": "ord-abc123",
  "customerId": "cust-001",
  "status": "PENDING",
  "total": 75000,
  "createdAt": "2026-06-02T10:00:00Z"
}
```
### Errores esperados
- 400 — campo requerido faltante
- 422 — regla de negocio violada (ej. orden sin ítems)
- 404 — orden no encontrada
- 409 — conflicto de estado (ej. ya cancelada)
- 500 — error interno (sin stack trace al consumidor)
## Preguntas de validación
1. ¿Por qué una API es un contrato y no solo una URL?
2. ¿Cuándo usarías 400, 401, 403, 404, 409 y 500?
3. ¿Qué problema resuelve HTTPS frente a HTTP?
## Mis notas y decisiones
> Registra aquí tus aprendizajes del día.
