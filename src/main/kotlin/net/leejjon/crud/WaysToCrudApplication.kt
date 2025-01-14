package net.leejjon.crud

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@OpenAPIDefinition
@EnableJpaRepositories(
	basePackages = [
		"net.leejjon.crud.database"
	]
)
class WaysToCrudApplication

fun main(args: Array<String>) {
	runApplication<WaysToCrudApplication>(*args)
}
