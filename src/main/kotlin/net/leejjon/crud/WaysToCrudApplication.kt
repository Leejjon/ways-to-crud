package net.leejjon.crud

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@OpenAPIDefinition
class WaysToCrudApplication

fun main(args: Array<String>) {
	runApplication<WaysToCrudApplication>(*args)
}
