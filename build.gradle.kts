plugins {
	kotlin("jvm") version "2.3.21"
	kotlin("plugin.spring") version "2.3.21"
	id("org.springframework.boot") version "3.4.1"
	id("io.spring.dependency-management") version "1.1.7"
	id("com.google.devtools.ksp") version "2.3.7"
}

group = "com.noovoweb"
version = "0.0.1-SNAPSHOT"
description = "Demo project for Spring Boot"

extra["kotlin.version"] = "2.3.21"
// kotlin-validator is compiled against coroutines 1.11.0; override Spring Boot's
// managed (older) version so the runtime matches and library classes load.
extra["kotlin-coroutines.version"] = "1.11.0"

val kotlinValidatorVersion: String by project

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
    // Uncomment to test a locally published library build (./gradlew publishToMavenLocal)
    // before it's released. Leave commented in commits so builds resolve from Maven Central.
    // mavenLocal()
    mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
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
