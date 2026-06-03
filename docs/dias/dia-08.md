# Día 8 — Manejo de datos: SQL, NoSQL y caché
## Concepto base
No todas las preguntas necesitan el mismo tipo de memoria. SQL es una biblioteca con catálogo estricto; NoSQL son archivadores flexibles; la caché es una libreta rápida para respuestas frecuentes.
## Temas
- Modelo relacional: tablas, relaciones, integridad referencial, transacciones ACID.
- Índices: cuándo usarlos, costo de escritura vs ganancia de lectura.
- Modelo documental (MongoDB): esquema flexible, consistencia eventual.
- Caché: Redis, TTL, estrategias de invalidación.
- Repositorios como adaptadores secundarios (hexagonal).
## Diseño de persistencia para órdenes
### Modelo relacional (PostgreSQL)
```sql
CREATE TABLE orders (
    id          UUID PRIMARY KEY,
    customer_id VARCHAR(100) NOT NULL,
    status      VARCHAR(20) NOT NULL,
    total       NUMERIC(15,2) NOT NULL,
    created_at  TIMESTAMP NOT NULL
);
CREATE TABLE order_items (
    id         UUID PRIMARY KEY,
    order_id   UUID REFERENCES orders(id),
    product_id VARCHAR(100) NOT NULL,
    quantity   INTEGER NOT NULL,
    unit_price NUMERIC(15,2) NOT NULL
);
CREATE INDEX idx_orders_customer_id ON orders(customer_id);
```
### Consulta órdenes por cliente
```sql
SELECT o.*, oi.* FROM orders o
JOIN order_items oi ON o.id = oi.order_id
WHERE o.customer_id = ?
ORDER BY o.created_at DESC;
```
### Estrategia de caché
- **Cachear:** listado de órdenes por cliente (GET /orders?customerId=X), datos de catálogo.
- **NO cachear:** estados en proceso de pago, datos de tarjetas, información sensible personal.
- **TTL:** 5 minutos para listados, 1 minuto para estados de orden.
- **Invalidación:** al crear o cancelar una orden, invalidar la clave `orders:customer:{customerId}`.
```
Redis key: orders:customer:cust-001
TTL: 300s
Invalidar en: POST /orders, PATCH /orders/{id}/cancel
```
## Preguntas de validación
1. ¿Cuándo usarías SQL y cuándo NoSQL?
2. ¿Qué riesgo tiene cachear información sensible?
3. ¿Qué problema resuelve un índice y qué costo puede tener?
## Mis notas y decisiones
> Registra aquí tus aprendizajes del día.
