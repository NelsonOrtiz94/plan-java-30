# Día 3 — Arquitectura hexagonal
## Concepto base
La arquitectura hexagonal protege el negocio del exterior. El dominio es el motor; los adaptadores son enchufes intercambiables. Si mañana cambia la base de datos, el caso de uso no debería enterarse.
## Temas
- Dominio: entidades, value objects, reglas de negocio puras, sin framework.
- Caso de uso: orquesta el dominio para cumplir una intención del negocio.
- Puertos de entrada: interfaces que el exterior usa para hablar con el caso de uso.
- Puertos de salida: interfaces que el caso de uso usa para hablar con el exterior.
- Adaptadores primarios: controllers, consumers (implementan puertos de entrada).
- Adaptadores secundarios: repositorios, clientes HTTP, publishers (implementan puertos de salida).
- Inversión de dependencias: el dominio define la interfaz; la infraestructura la implementa.
## Diagrama conceptual
```
[Controller]──usa──>[CreateOrderPort]──implementa──>[CreateOrderUseCase]
                                                           │ usa
                                                  [OrderRepositoryPort]
                                                           │ implementa
                                              [InMemoryOrderRepository]
```
## Estructura de paquetes
```
orders-domain/
  model/        Order, OrderItem, OrderStatus, Money, CustomerId
  exception/    OrderDomainException, EmptyOrderException, InvalidQuantityException
orders-application/
  port/input/   CreateOrderPort, FindOrdersByCustomerPort
  port/output/  OrderRepositoryPort
  usecase/      CreateOrderUseCase, FindOrdersByCustomerUseCase
orders-infrastructure/
  adapter/web/            OrderController, dto/, mapper/
  adapter/persistence/    InMemoryOrderRepository
  config/                 BeanConfiguration
```
## Reglas de negocio del dominio Order
1. Debe tener al menos un ítem → EmptyOrderException.
2. Cada ítem con cantidad > 0 → InvalidQuantityException.
3. Total = suma(quantity × unitPrice).
4. Estado inicial = PENDING.
5. Solo se puede cancelar en estado PENDING.
## Preguntas de validación
1. ¿Por qué el dominio no debe depender del framework?
2. ¿Qué diferencia hay entre puerto y adaptador?
3. ¿Dónde ubicarías una integración con un proveedor de pagos?
## Mis notas y decisiones
> Registra aquí tus aprendizajes del día.
