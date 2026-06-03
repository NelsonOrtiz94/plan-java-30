# Día 6 — Pruebas unitarias backend
## Concepto base
Una prueba es un contrato ejecutable. Si la documentación dice cómo debería comportarse el sistema, la prueba lo verifica automáticamente.
## Temas
- Pirámide de pruebas: unitarias, integración, e2e.
- Pruebas unitarias: JUnit 5, AssertJ.
- Dobles de prueba: mocks, stubs, fakes.
- Mockito: @Mock, @InjectMocks, when/thenReturn, verify.
- Pruebas de servicios (casos de uso).
- Pruebas de controladores: MockMvc / WebTestClient.
- Cobertura útil vs cobertura cosmética.
## Diferencias: mock vs stub vs fake
| Tipo | Descripción | Uso |
|------|------------|-----|
| Stub | Devuelve valores fijos | Simular repositorio con datos |
| Mock | Verifica interacciones | Verificar que se llamó un puerto |
| Fake | Implementación real simplificada | InMemoryOrderRepository en pruebas |
## Actividad práctica
### Pruebas del caso de uso CreateOrderUseCase
```java
@Test void shouldCalculateTotalCorrectly() { ... }
@Test void shouldThrowWhenOrderHasNoItems() { ... }
@Test void shouldThrowWhenQuantityIsZero() { ... }
@Test void shouldSaveOrderThroughRepositoryPort() { ... }
```
## Preguntas de validación
1. ¿Qué diferencia hay entre mock, stub y fake?
2. ¿Qué pruebas pertenecen al dominio y cuáles a infraestructura?
3. ¿Por qué una cobertura alta no garantiza calidad?
## Mis notas y decisiones
> Registra aquí tus aprendizajes del día.
