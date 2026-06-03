# Día 4 — Programación funcional en Java 17
## Concepto base
La programación funcional transforma datos evitando efectos secundarios innecesarios.
## Temas
- Lambdas, Functional interfaces (Function, Predicate, Consumer, Supplier).
- Streams: pipeline declarativo de transformaciones.
- Operaciones intermedias: map, filter, flatMap, sorted, distinct.
- Operaciones terminales: collect, reduce, forEach, count, anyMatch, findFirst.
- Pure functions, High order functions, Inmutabilidad (records, List.of()).
- Composición: Function.andThen(), Predicate.and().
- Evitar efectos secundarios dentro de streams.
## Actividad práctica
### Calcular total con funciones puras
```java
BigDecimal total = order.getItems().stream()
    .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
    .reduce(BigDecimal.ZERO, BigDecimal::add);
```
### Filtrar disponibles
```java
Predicate<OrderItem> isAvailable = item -> item.getQuantity() > 0;
List<OrderItem> available = items.stream().filter(isAvailable).toList();
```
### Agrupar por categoría
```java
Map<String, List<Product>> byCategory =
    products.stream().collect(Collectors.groupingBy(Product::getCategory));
```
### Descuentos componibles
```java
Function<BigDecimal, BigDecimal> disc10 = p -> p.multiply(new BigDecimal("0.90"));
Function<BigDecimal, BigDecimal> disc5  = p -> p.multiply(new BigDecimal("0.95"));
BigDecimal final = disc10.andThen(disc5).apply(original);
```
### Antipatrón — efecto secundario en stream
```java
// MAL
items.stream().map(i -> { log.add(i.getId()); return i; }).toList();
// BIEN
List<OrderItem> filtered = items.stream().filter(i -> i.getQuantity() > 0).toList();
filtered.forEach(i -> log.add(i.getId()));
```
## Preguntas de validación
1. ¿Qué hace que una función sea pura?
2. ¿Por qué un stream con efectos secundarios puede ser peligroso?
3. ¿Cuándo preferirías una colección tradicional frente a un stream?
## Mis notas y decisiones
> Registra aquí tus aprendizajes del día.
