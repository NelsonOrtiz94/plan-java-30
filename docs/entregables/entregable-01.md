# Entregable 1 — API base de órdenes con arquitectura hexagonal (Día 5)
## Historia de usuario
Como usuario del sistema de compras,
quiero crear una orden con productos, cantidades y datos del comprador,
para iniciar el proceso de compra de forma controlada y validada.
## Criterios de aceptación
- [ ] Exponer API POST /api/v1/orders.
- [ ] Validar que la orden tenga al menos un producto.
- [ ] Validar cantidades mayores a cero.
- [ ] Calcular el total de la orden en el dominio.
- [ ] El dominio NO depende del framework web.
- [ ] Existen puertos de entrada y salida.
- [ ] Existe adaptador web (Controller).
- [ ] Existe adaptador de persistencia en memoria.
- [ ] Existe documentación básica del contrato (OpenAPI o colección).
- [ ] Existen pruebas unitarias del caso de uso principal.
## Evidencias esperadas
- [ ] Diagrama simple de arquitectura (puede ser ASCII o imagen).
- [ ] Código fuente organizado por capas (domain / application / infrastructure).
- [ ] Pruebas unitarias ejecutables (`./gradlew test`).
- [ ] Swagger UI accesible o contrato YAML.
- [ ] README con decisiones técnicas.
## Checklist de revisión técnica
El desarrollador debe poder explicar:
| Pregunta | Respuesta |
|----------|-----------|
| ¿Qué parte representa el dominio? | |
| ¿Qué parte representa el caso de uso? | |
| ¿Qué puertos creó y para qué? | |
| ¿Qué adaptadores implementó? | |
| ¿Qué regla de negocio probó? | |
| ¿Qué decisiones tomó para evitar acoplamiento? | |
## Ubicación del código
`orders-platform/` (módulos: orders-domain, orders-application, orders-infrastructure)
