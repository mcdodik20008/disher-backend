import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    kotlin("plugin.jpa") version "1.9.25"
    id("org.springframework.boot") version "3.3.2"
    id("io.spring.dependency-management") version "1.1.5"
    id("com.google.cloud.tools.jib") version "3.4.3"
}

group = "com.mcdodik"
version = "0.0.1-SNAPSHOT"
description = "disher"

java.sourceCompatibility = JavaVersion.VERSION_21

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Базовые стартеры
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // JPA + PostgreSQL
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-configuration-processor")
    implementation("org.projectlombok:lombok")
    runtimeOnly("org.postgresql:postgresql:42.7.4")

    // Liquibase
    implementation("org.liquibase:liquibase-core:4.28.0")

    // Kotlin / Jackson
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

/**
 * Jib: собираем и пушим контейнер одной командой:
 *   ./gradlew jib -Djib.to.image=docker.io/<your_user>/disher:latest -Djib.to.auth.username=<user> -Djib.to.auth.password=<token>
 *
 * Профиль "docker" можно включать переменной SPRING_PROFILES_ACTIVE=docker
 */
jib {
    from {
        image = "eclipse-temurin:21-jre"
    }
    to {
        image = "docker.io/your-dockerhub-user/disher:latest" // переопредели через -Djib.to.image=...
        // учётки лучше передавать через -D параметрами/CI secrets
    }
    container {
        jvmFlags = listOf("-XX:+UseG1GC", "-XX:MaxRAMPercentage=75")
        ports = listOf("8080")
        environment = mapOf("SPRING_PROFILES_ACTIVE" to "docker")
    }
}

tasks.named<BootBuildImage>("bootBuildImage") {
    // Позволяем переопределить имя образа через -PimageName=...
    val fallback = "docker.io/${System.getenv("DOCKERHUB_USERNAME") ?: "mcdodik"}/disher:${project.version}"
    imageName.set(providers.gradleProperty("imageName").orElse(fallback))

    // Публиковать будем руками через docker push, поэтому тут publish=false
    publish.set(false)
}
