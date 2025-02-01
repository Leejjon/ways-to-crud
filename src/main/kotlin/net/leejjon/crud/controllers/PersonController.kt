package net.leejjon.crud.controllers

import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import net.leejjon.crud.database.DbService
import net.leejjon.crud.model.NewPerson
import net.leejjon.crud.model.Person
import net.leejjon.crud.model.Persons
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController("/v1")
class PersonController(
    private val dbService: DbService,
) {
    @GetMapping("/v1/persons")
    fun listPersons(): ResponseEntity<Persons> {
        return ResponseEntity.ok(Persons(dbService.getPersonsFromDb()))
    }

//    @ApiResponses(value =
//        [
//            ApiResponse(responseCode = "200"),
//            ApiResponse(responseCode = "404", description = "User not found"),
//            ApiResponse(responseCode = "500", description = "Internal Server Error")
//        ]
//    )
//    @GetMapping("/v1/persons/{id}")
//    fun getPerson(
//        @PathVariable
//        id: Int
//    ): ResponseEntity<Person> {
//        val potentiallyExistingPerson = dbService.getPerson(id)
//        return if (potentiallyExistingPerson.isPresent) {
//            ResponseEntity.ok(potentiallyExistingPerson.get())
//        } else {
//            ResponseEntity.notFound().build()
//        }
//    }
//
//    @ApiResponses(value =
//        [
//            ApiResponse(responseCode = "200"),
//            ApiResponse(responseCode = "500", description = "Internal Server Error")
//        ]
//    )
//    @PostMapping("/v1/persons")
//    fun createPerson(@RequestBody person: NewPerson): ResponseEntity<Person> {
//        val createdPerson = dbService.createPerson(person)
//        return ResponseEntity.ok().body(createdPerson)
//    }
//
//    @ApiResponses(value =
//        [
//            ApiResponse(responseCode = "200"),
//            ApiResponse(responseCode = "404", description = "User not found"),
//            ApiResponse(responseCode = "500", description = "Internal Server Error")
//        ]
//    )
//    @DeleteMapping("/v1/persons/{id}")
//    fun deletePerson(
//        @PathVariable id: Int
//    ) {
//        dbService.deletePerson(id)
//    }
//
//    @PutMapping("/v1/persons")
//    fun updatePerson(
//        @RequestBody person: Person
//    ): ResponseEntity<Person> = ResponseEntity.ok(dbService.updatePerson(person))
}
