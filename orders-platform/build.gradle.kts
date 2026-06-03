import org.gradle.api.tasks.testing.logging.TestExceptionFormat
plugins {
    java
    id("org.springframework.boot") version "3.2.5" apply false
    id("io.spring.dependency-management") version "1.1.4" apply false
}
allprojects {
    group = "com.bancolombia"
    version = "1.0.0-SNAPSHOT"
    repositories {
        mavenCentral()
    }
}
subprojects {
    apply(plugin = "java")
    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }
    dependencies {
        // Testing
        testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
        testImplementation("org.assertj:assertj-core:3.25.3")
        testImplementation("org.mockito:mockito-core:5.11.0")
        testImplementation("org.mockito:mockito-junit-jupiter:5.11.0")
    }
    tasks.withType<Test> {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
            exceptionFormat = TestExceptionFormat.FULL
        }
    }
}
