package net.leejjon.crud.controllers

import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import net.leejjon.crud.database.DatabaseService
import net.leejjon.crud.model.Person
import net.leejjon.crud.model.Persons
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController("/v1")
class CrudController(
    private val databaseService: DatabaseService,
) {
    @GetMapping("/v1/persons")
    fun listPersons(): ResponseEntity<Persons> {
        return ResponseEntity.ok(Persons(databaseService.getPersonsFromDb()))
    }

    @GetMapping("/v1/persons/{id}")
    fun getPerson(
        @PathVariable
        id: Int
    ): ResponseEntity<Person> {
        databaseService.getPerson(id)?.let {
            return ResponseEntity.ok(it)
        }
        return ResponseEntity.notFound().build()
    }

    @ApiResponses(value =
        [
            ApiResponse(responseCode = "200"),
            ApiResponse(responseCode = "500", description = "Internal Server Error")
        ]
    )
    @PostMapping("/v1/persons")
    fun createPerson(@RequestBody person: Person): ResponseEntity<Person> {
        val createdPerson = databaseService.createPerson(person)
        return ResponseEntity.ok().body(createdPerson)
    }
}
