package net.leejjon.crud

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import org.jetbrains.exposed.spring.autoconfigure.ExposedAutoConfiguration
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@OpenAPIDefinition
@ImportAutoConfiguration(ExposedAutoConfiguration::class)
class WaysToCrudApplication

fun main(args: Array<String>) {
	runApplication<WaysToCrudApplication>(*args)
}
