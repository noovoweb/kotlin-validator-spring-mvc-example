plugins {
	kotlin("jvm") version "2.3.10"
	kotlin("plugin.spring") version "2.3.10"
	id("org.springframework.boot") version "3.4.1"
	id("io.spring.dependency-management") version "1.1.7"
	id("com.google.devtools.ksp") version "2.3.6"
}

group = "com.noovoweb"
version = "0.0.1-SNAPSHOT"
description = "Demo project for Spring Boot"

extra["kotlin.version"] = "2.3.10"

val kotlinValidatorVersion = "0.1.0-beta.1"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
    mavenLocal()
    mavenCentral()
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/noovoweb/kotlin-validator")
        credentials {
            username = providers.gradleProperty("gpr.user").orNull
                ?: System.getenv("GITHUB_ACTOR")
            password = providers.gradleProperty("gpr.token").orNull
                ?: System.getenv("GITHUB_TOKEN")
        }
    }
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.8.0")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.3")

    implementation("com.noovoweb:kotlin-validator-annotations:$kotlinValidatorVersion")
    implementation("com.noovoweb:kotlin-validator-runtime:$kotlinValidatorVersion")
    implementation("com.noovoweb:kotlin-validator-spring-mvc:$kotlinValidatorVersion")
    ksp("com.noovoweb:kotlin-validator-processor:$kotlinValidatorVersion")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
	jvmToolchain(21)
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
	sourceSets.main {
		kotlin.srcDir("build/generated/ksp/main/kotlin")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
