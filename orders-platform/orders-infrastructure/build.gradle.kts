plugins {
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.4"
}
dependencies {
    implementation(project(":orders-domain"))
    implementation(project(":orders-application"))
    // Spring Boot Web MVC
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    // OpenAPI / Swagger UI
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")
    // Reactor Core — programación reactiva (Flux / Mono) compatible con Spring MVC
    implementation("io.projectreactor:reactor-core")
    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
}