// orders-application: casos de uso, depende solo del dominio
plugins {
    id("io.spring.dependency-management") version "1.1.4"
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:${property("springBootVersion")}")
    }
}

dependencies {
    implementation(project(":orders-domain"))
    // Reactor Core — para definir puertos reactivos (Flux/Mono) sin depender del framework web
    implementation("io.projectreactor:reactor-core")
    // Reactor Test — StepVerifier para pruebas reactivas
    testImplementation("io.projectreactor:reactor-test")
}
