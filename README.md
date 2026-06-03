# Plan de Formación Java 30 Días

> **Stack:** Java 17 · Gradle (Kotlin DSL) · Spring Boot 3 · Spring Cloud · WebFlux · Project Reactor · Kafka · RabbitMQ · Docker · Kubernetes · AWS  
> **Estilo:** Simulación del stack de Bancolombia — arquitectura hexagonal, eventos, reactive, DevOps.

---

## Índice del plan

| Semana | Días | Enfoque principal | Rama |
|--------|------|-------------------|------|
| Semana 1 | 1 – 5 | Fundamentos, arquitectura hexagonal, programación funcional | `semana-1` |
| Semana 2 | 6 – 10 | Pruebas, datos, documentación, reactive básico | `semana-2` |
| Semana 3 | 11 – 15 | WebFlux, pruebas reactivas, eventos, brokers | `semana-3` |
| Semana 4 | 16 – 20 | Kafka, resiliencia, Spring Cloud, observabilidad | `semana-4` |
| Semana 5 | 21 – 30 | Docker, CI/CD, Kubernetes, AWS, cierre | `semana-5` |

---

## Tabla de entregables

| Entregable | Día | Historia de usuario | Enfoque |
|-----------|-----|---------------------|---------|
| Entregable 1 | Día 5 | Crear una orden con productos | Hexagonal, dominio, API, pruebas unitarias |
| Entregable 2 | Día 10 | Consultar órdenes de un cliente | Persistencia, documentación, pruebas |
| Entregable 3 | Día 15 | Publicar evento OrdenCreada | Reactive, eventos, broker simulado |
| Entregable 4 | Día 20 | Coordinar inventario, pago y notificación | Broker, idempotencia, DLQ, trazabilidad |
| Entregable 5 | Día 25 | Contenerizar y desplegar el servicio | DevOps, Docker, CI/CD, Kubernetes |
| Entregable 6 | Día 30 | Solución integral lista para revisión | Arquitectura, seguridad, nube, operación |

---

## Proyecto guía: `orders-platform`

Plataforma modular de gestión de órdenes de compra con los siguientes módulos:

```
orders-platform/
├── orders-domain/          ← entidades, VOs, puertos, reglas (sin framework)
├── orders-application/     ← casos de uso (depende solo de domain)
├── orders-infrastructure/  ← adaptadores web, persistencia, broker, caché
├── catalog-domain/
├── inventory-domain/
├── payments-domain/
└── notifications-domain/
```

### Cómo ejecutar localmente

```bash
cd orders-platform
./gradlew bootRun
```

### Cómo correr las pruebas

```bash
cd orders-platform
./gradlew test
```

---

## Convenciones del repositorio

### Ramas
- `main` → rama de integración, solo recibe PRs aprobados al finalizar cada semana.
- `semana-N` → rama de trabajo de cada semana, se crea desde `main`.

### Commits
Usar prefijos semánticos:
- `feat:` nueva funcionalidad
- `test:` pruebas
- `docs:` documentación
- `refactor:` refactoring sin cambio funcional
- `chore:` configuración, build

### Estructura de carpetas
```
plan-java-30/
├── README.md
├── docs/
│   ├── dias/          ← dia-01.md … dia-30.md (teoría + actividades)
│   ├── entregables/   ← entregable-01.md … entregable-06.md
│   └── seguimientos/  ← seguimiento-01.md … seguimiento-04.md
├── orders-platform/   ← proyecto Gradle multi-módulo (crece progresivamente)
└── .github/
    └── workflows/
        └── ci.yml
```

---

## Stack tecnológico

| Categoría | Tecnología |
|-----------|-----------|
| Lenguaje | Java 17 |
| Build | Gradle 8 (Kotlin DSL) |
| Framework | Spring Boot 3.x |
| Web reactivo | Spring WebFlux + Project Reactor |
| Mensajería | RabbitMQ (semana 3) · Kafka (semana 4) |
| Persistencia | PostgreSQL (R2DBC reactivo) · Redis (caché) |
| Documentación | SpringDoc OpenAPI 3 |
| Pruebas | JUnit 5 · Mockito · AssertJ · StepVerifier |
| Contenedores | Docker · Docker Compose |
| Orquestación | Kubernetes |
| Nube | AWS (ECS/EKS, SQS, RDS, S3, CloudWatch) |
| CI/CD | GitHub Actions |

