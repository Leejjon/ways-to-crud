plugins {
	kotlin("jvm") version "1.9.24"
	kotlin("plugin.spring") version "1.9.24"
	id("org.springframework.boot") version "3.3.2"
	id("io.spring.dependency-management") version "1.1.6"
	id("org.jooq.jooq-codegen-gradle") version "3.19.18"
}

group = "net.leejjon"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
	sourceSets["main"].java {
		// Add the generated jOOQ source folder to the main source set
		srcDir("$buildDir/generated-src/jooq")
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
//	implementation("org.springframework.boot:spring-boot-starter-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-jooq")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	// https://springdoc.org/#general-overview
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
	implementation("io.github.oshai:kotlin-logging-jvm:7.0.0")
	implementation("org.jooq:jooq:3.19.18")
	implementation("org.jooq:jooq-meta-extensions:3.19.18")

	runtimeOnly("com.h2database:h2")
	jooqCodegen("org.jooq:jooq-meta-extensions:3.19.18")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testImplementation ("io.rest-assured:rest-assured:5.5.0")
	testImplementation ("io.rest-assured:kotlin-extensions:5.5.0")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.named("compileKotlin") {
	dependsOn("jooqCodegen") // Ensures jOOQ code generation runs before Kotlin compilation
}

jooq {
	configuration {
		generator {
			name = "org.jooq.codegen.JavaGenerator"
			database {
				name = "org.jooq.meta.extensions.ddl.DDLDatabase"
				properties {

					// Specify the location of your SQL script.
					// You may use ant-style file matching, e.g. /path/**/to/*.sql
					//
					// Where:
					// - ** matches any directory subtree
					// - * matches any number of characters in a directory / file name
					// - ? matches a single character in a directory / file name
					property {
						key = "scripts"
						value = "src/main/resources/schema.sql"
					}

					// The sort order of the scripts within a directory, where:
					//
					// - semantic: sorts versions, e.g. v-3.10.0 is after v-3.9.0 (default)
					// - alphanumeric: sorts strings, e.g. v-3.10.0 is before v-3.9.0
					// - flyway: sorts files the same way as flyway does
					// - none: doesn't sort directory contents after fetching them from the directory
					property {
						key = "sort"
						value = "semantic"
					}

					// The default schema for unqualified objects:
					//
					// - public: all unqualified objects are located in the PUBLIC (upper case) schema
					// - none: all unqualified objects are located in the default schema (default)
					//
					// This configuration can be overridden with the schema mapping feature
					property {
						key = "unqualifiedSchema"
						value = "none"
					}

					// The default name case for unquoted objects:
					//
					// - as_is: unquoted object names are kept unquoted
					// - upper: unquoted object names are turned into upper case (most databases)
					// - lower: unquoted object names are turned into lower case (e.g. PostgreSQL)
					property {
						key = "defaultNameCase"
						value = "as_is"
					}
				}
			}
			target {
				packageName = "net.leejjon.crud.database.model"
				directory = "$buildDir/generated-src/jooq"
			}
		}
	}
}


