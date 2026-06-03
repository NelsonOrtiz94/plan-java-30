# Día 2 — Clean Code, seguridad y fundamentos de ingeniería de software
## Concepto base
El software debe ser mantenible, no solo funcional. Código limpio es como una cocina profesional: cada herramienta tiene lugar, los procesos son repetibles y cualquier chef puede continuar el trabajo.
## Temas
- Ciclo de vida del software: análisis, diseño, implementación, pruebas, mantenimiento y soporte.
- Clean Code: nombres significativos, funciones pequeñas, sin comentarios redundantes.
- Bajo acoplamiento y alta cohesión.
- Principios SOLID aplicados al backend Java.
- OWASP Top 10.
- SQL Injection: parametrización, JPA como protección.
- XSS: validación y encoding de salida.
- Validación de entrada: Bean Validation, validación en dominio vs. validación de API.
- Manejo seguro de errores: códigos funcionales sin stack trace.
- Secretos y configuración segura: variables de entorno, AWS Secrets Manager.
## Actividad práctica
### Lista de riesgos para la API de órdenes
| Riesgo | Tipo OWASP | Mitigación |
|--------|-----------|-----------|
| SQL Injection en queries por customerId | A03 | Repositorios JPA, sin concatenación |
| Stack trace expuesto en error 500 | A05 | Handler global con código funcional |
| JWT sin validar expiración | A07 | Validar claim `exp` en cada request |
| Cantidad negativa en ítem | Validación dominio | Regla en entidad Order |
| customerId manipulado | A01 | Validar que token = customerId |
| Credenciales en código fuente | A02 | Variables de entorno o Secrets Manager |
### Respuestas de error seguras
```json
// MAL - expone detalles internos
{ "error": "org.springframework.dao.DataAccessException: could not execute..." }
// BIEN - código funcional limpio
{
  "code": "ORDER_CREATION_FAILED",
  "message": "No fue posible registrar la orden. Intente nuevamente.",
  "traceId": "abc-123-xyz"
}
```
## Preguntas de validación
1. ¿Por qué no se debe mostrar un stack trace al consumidor de una API?
2. ¿Cuál es la diferencia entre validación, sanitización y autorización?
3. ¿Cómo se previene una inyección SQL desde el diseño?
## Mis notas y decisiones
> Registra aquí tus aprendizajes del día.
