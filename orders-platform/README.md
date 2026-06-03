# orders-platform

> Proyecto guía del plan de formación Java 30 días.  
> Plataforma modular de gestión de órdenes de compra construida con arquitectura hexagonal, Java 17 y Spring Boot 3.2.5.

---

## Arquitectura

El proyecto implementa **Arquitectura Hexagonal (Ports & Adapters)** organizada en tres módulos Gradle independientes:

```
orders-platform/
├── orders-domain/          ← Dominio puro: entidades, objetos de valor, reglas de negocio (sin framework)
├── orders-application/     ← Casos de uso, puertos de entrada y salida (depende solo del dominio)
└── orders-infrastructure/  ← Adaptadores web (REST), persistencia, configuración Spring Boot
```

### Flujo de dependencias

```
[HTTP Client]
      │
      ▼
[OrderController]                       ← Adaptador primario (web)
      │
      ▼
[CreateOrderPort]                       ← Puerto de entrada
[FindOrdersByCustomerPort]              ← Puerto de entrada (bloqueante)
[FindOrdersByCustomerReactivePort]      ← Puerto de entrada (reactivo — Día 9)
      │
      ▼
[CreateOrderUseCase]
[FindOrdersByCustomerUseCase]
[FindOrdersByCustomerReactiveUseCase]   ← Caso de uso reactivo (Día 9)
      │
      ▼
[OrderRepositoryPort]                   ← Puerto de salida
      │
      ▼
[InMemoryOrderRepository]               ← Adaptador secundario (persistencia en memoria)
```

**Regla de oro:** `orders-domain` no tiene ninguna dependencia de framework.  
`orders-application` solo depende de `orders-domain` y Reactor Core.  
Solo `orders-infrastructure` depende de Spring Boot.

---

## Stack tecnológico

| Tecnología        | Versión | Uso                                  |
|-------------------|---------|--------------------------------------|
| Java              | 17      | Lenguaje principal                   |
| Spring Boot       | 3.2.5   | Framework web e infraestructura      |
| Reactor Core      | 3.6.x   | Programación reactiva (Flux / Mono)  |
| springdoc-openapi | 2.3.0   | Documentación OpenAPI / Swagger UI   |
| JUnit Jupiter     | 5.10.2  | Pruebas unitarias                    |
| Mockito           | 5.11.0  | Dobles de prueba (mocks)             |
| Reactor Test      | 3.6.x   | StepVerifier para pruebas reactivas  |
| AssertJ           | 3.25.3  | Aserciones fluidas                   |
| Gradle            | 8.7     | Build system (Kotlin DSL)            |

---

## Módulos

### `orders-domain` — Día 3, Día 4

Dominio puro sin ninguna dependencia de framework. Contiene todas las reglas de negocio.

| Clase                       | Tipo              | Responsabilidad                                        |
|-----------------------------|-------------------|--------------------------------------------------------|
| `Order`                     | Entidad raíz      | Crear órdenes, calcular total, gestionar estado        |
| `OrderItem`                 | Objeto de valor   | Producto, cantidad, precio unitario                    |
| `Money`                     | Objeto de valor   | Monto inmutable con precisión decimal (`BigDecimal`)   |
| `CustomerId`                | Objeto de valor   | Identificador del cliente                              |
| `OrderStatus`               | Enum              | `PENDING`, `CANCELLED`                                 |
| `EmptyOrderException`       | Excepción dominio | Una orden debe tener al menos un producto              |
| `InvalidQuantityException`  | Excepción dominio | La cantidad debe ser mayor a cero                      |
| `OrderNotFoundException`    | Excepción dominio | Orden no encontrada por ID                             |
| `OrderDomainException`      | Excepción base    | Base con código y mensaje para el manejador de errores |

**Programación funcional aplicada (Día 4):**
- `Money` es inmutable: todos sus métodos devuelven una nueva instancia.
- `calculateTotal` usa `stream().map().reduce()` — función pura sin efectos secundarios.
- `Order.create()` valida precondiciones antes de construir el objeto.

### `orders-application` — Día 3, Día 9

| Clase                                 | Tipo              | Responsabilidad                                     |
|---------------------------------------|-------------------|-----------------------------------------------------|
| `CreateOrderPort`                     | Puerto de entrada | Crear una orden                                     |
| `FindOrdersByCustomerPort`            | Puerto de entrada | Consulta bloqueante de órdenes por cliente          |
| `FindOrdersByCustomerReactivePort`    | Puerto de entrada | Consulta reactiva: retorna `Flux<Order>` (Día 9)    |
| `OrderRepositoryPort`                 | Puerto de salida  | `save`, `findById`, `findByCustomerId`              |
| `CreateOrderUseCase`                  | Caso de uso       | Valida, crea `Order` y delega al repositorio        |
| `FindOrdersByCustomerUseCase`         | Caso de uso       | Consulta bloqueante a través del puerto de salida   |
| `FindOrdersByCustomerReactiveUseCase` | Caso de uso       | Envuelve la consulta en `Flux` con scheduler de I/O |

### `orders-infrastructure` — Día 3, Día 7

| Clase                     | Responsabilidad                                                   |
|---------------------------|-------------------------------------------------------------------|
| `OrderController`         | Adaptador REST: endpoint bloqueante y reactivo (NDJSON streaming) |
| `InMemoryOrderRepository` | Persistencia en memoria con `ConcurrentHashMap`                   |
| `GlobalExceptionHandler`  | Manejo centralizado de errores sin exponer detalles internos      |
| `BeanConfiguration`       | Cableado explícito de dependencias sin `@Component` en el dominio |
| `OrderWebMapper`          | Transformación entre DTOs y modelos de dominio                    |

---

## API REST

**Base URL:** `http://localhost:8080/api/v1`  
**Documentación interactiva:** `http://localhost:8080/swagger-ui.html`

| Método | Endpoint                      | Descripción                                 | Código exitoso |
|--------|-------------------------------|---------------------------------------------|----------------|
| `POST` | `/orders`                     | Crear una nueva orden                       | `201 Created`  |
| `GET`  | `/orders?customerId=X`        | Consultar órdenes por cliente (bloqueante)  | `200 OK`       |
| `GET`  | `/orders/stream?customerId=X` | Consultar órdenes como flujo NDJSON (Día 9) | `200 OK`       |

### Códigos de error

| HTTP  | Cuándo ocurre                                        |
|-------|------------------------------------------------------|
| `400` | Validación de entrada fallida (`customerId` vacío)   |
| `404` | Orden no encontrada                                  |
| `422` | Regla de negocio violada (orden vacía, qty inválida) |
| `500` | Error no controlado                                  |

### Ejemplo — crear una orden

**Request:**
```json
POST /api/v1/orders
{
  "customerId": "cust-001",
  "items": [
    { "productId": "prod-001", "quantity": 2, "unitPrice": 15000.00 },
    { "productId": "prod-002", "quantity": 1, "unitPrice": 45000.00 }
  ]
}
```

**Response `201 Created`:**
```json
{
  "orderId": "a1b2c3d4-...",
  "customerId": "cust-001",
  "status": "PENDING",
  "total": 75000.00,
  "createdAt": "2026-06-02T19:00:00Z",
  "items": [
    { "productId": "prod-001", "quantity": 2, "unitPrice": 15000.00, "subtotal": 30000.00 },
    { "productId": "prod-002", "quantity": 1, "unitPrice": 45000.00, "subtotal": 45000.00 }
  ]
}
```

---

## Reglas de negocio — Días 1, 2, 3

1. Una orden debe tener **al menos un producto** → `EmptyOrderException` (422)
2. La cantidad de cada item debe ser **mayor a cero** → `InvalidQuantityException` (422)
3. El `customerId` no puede ser nulo → `NullPointerException` (400)
4. El total = suma de subtotales: `cantidad × precio unitario`
5. Solo se puede cancelar una orden en estado `PENDING` → `OrderDomainException` (422)
6. Los errores **no exponen stack trace** al consumidor — OWASP Top 10 (Día 2)

---

## Pruebas — Día 6

| Clase de prueba                           | Módulo         | Tipo                      | Tests |
|-------------------------------------------|----------------|---------------------------|-------|
| `OrderTest`                               | domain         | Unitaria (dominio puro)   | 8     |
| `CreateOrderUseCaseTest`                  | application    | Unitaria con Mockito      | 4     |
| `FindOrdersByCustomerReactiveUseCaseTest` | application    | Reactiva con StepVerifier | 4     |
| `OrderControllerTest`                     | infrastructure | Web con MockMvc           | 3     |

```bash
./gradlew test
```

---

## Programación reactiva — Día 9

| Concepto           | Implementación                                                                     |
|--------------------|------------------------------------------------------------------------------------|
| **Publisher**      | `Flux<Order>` emite órdenes elemento a elemento                                    |
| **No bloqueante**  | `Schedulers.boundedElastic()` aísla la llamada bloqueante al repositorio           |
| **Backpressure**   | Spring MVC + Reactor respetan la velocidad del consumidor vía NDJSON              |
| **Flujo finito**   | `flatMapIterable` convierte `List<Order>` en `Flux<Order>` con señal `onComplete` |
| **Error reactivo** | `Flux.error()` propaga errores como señal del flujo, no como excepción síncrona   |
| **StepVerifier**   | Pruebas declarativas: `expectNext`, `verifyComplete`, `verifyError`               |

```
GET /orders         → espera TODA la lista → serializa → responde de una vez  (bloqueante)
GET /orders/stream  → emite cada Order a medida que el Flux la produce         (NDJSON streaming)
```

**Antipatrón que se evita:**
```java
// MAL — bloquea el scheduler de Reactor
.map(order -> externalService.getDetails(order.getId()).block())

// BIEN — componer con flatMap
.flatMap(order -> externalService.getDetails(order.getId()))
```

---

## Persistencia y caché — Día 8

### Decisión actual: repositorio en memoria

Se usa `InMemoryOrderRepository` (`ConcurrentHashMap`) porque:
- Permite enfocarse en reglas de negocio sin dependencias externas.
- `OrderRepositoryPort` abstrae el mecanismo: **cambiar a SQL o NoSQL no requiere modificar ningún caso de uso**.

### Evolución planificada

| Fase      | Tecnología                      | Justificación                                               |
|-----------|---------------------------------|-------------------------------------------------------------|
| Actual    | In-memory (`ConcurrentHashMap`) | Desarrollo y pruebas unitarias                              |
| Siguiente | PostgreSQL + Spring Data JPA    | Órdenes requieren ACID, relaciones e integridad referencial |
| Reactivo  | PostgreSQL + R2DBC              | Consultas no bloqueantes (Semana 3)                         |
| Caché     | Redis + TTL                     | Consultas frecuentes de órdenes recientes por cliente       |

### Estrategia de caché

- **Qué cachear:** resultado de `findByCustomerId` para clientes activos.
- **TTL:** 5 minutos.
- **Invalidación:** al crear o cancelar una orden del cliente afectado.
- **Qué NO cachear:** datos de pago, tokens, información sensible (OWASP — Día 2).

---

## Ejecutar el proyecto

```bash
# Compilar y ejecutar todas las pruebas
./gradlew build

# Iniciar la aplicación
./gradlew :orders-infrastructure:bootRun

# Swagger UI
open http://localhost:8080/swagger-ui.html
```

---

## Decisiones técnicas clave

| Decisión                          | Alternativa descartada            | Razón                                                                    |
|-----------------------------------|-----------------------------------|--------------------------------------------------------------------------|
| Módulos Gradle separados por capa | Paquetes en un solo módulo        | Fuerza las dependencias: `domain` no puede importar Spring aunque quiera |
| Constructor injection             | Field injection con `@Autowired`  | Permite instanciar casos de uso en tests sin levantar Spring             |
| `BeanConfiguration` explícita     | `@Component` + `@Service`         | Control total del cableado, sin anotaciones de framework en el dominio   |
| Records para DTOs                 | Clases con getters/setters        | Inmutabilidad y menos boilerplate en Java 17                             |
| `Flux.error()` para null reactivo | `Objects.requireNonNull` síncrono | El error debe ser señal del flujo para que `StepVerifier` lo capture     |
| NDJSON para endpoint reactivo     | `application/json` lista completa | Streaming real: el cliente recibe cada elemento apenas está listo        |
| Errores sin stack trace           | Respuesta con detalles internos   | Seguridad — OWASP: no exponer información del sistema al consumidor      |

