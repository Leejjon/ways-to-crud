package net.leejjon.crud.controllers

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

    @GetMapping("/v1/persons/{id}")
    fun getPerson(
        @PathVariable
        id: Int
    ): ResponseEntity<Person> {
        dbService.getPerson(id)?.let {
            return ResponseEntity.ok(it)
        }
        return ResponseEntity.notFound().build()
    }

    @PostMapping("/v1/persons")
    fun createPerson(@RequestBody person: NewPerson): ResponseEntity<Person> {
        val createdPerson = dbService.createPerson(person)
        return ResponseEntity.ok().body(createdPerson)
    }

    @DeleteMapping("/v1/persons/{id}")
    fun deletePerson(
        @PathVariable id: Int
    ) {
        dbService.deletePerson(id)
    }

    @PutMapping("/v1/persons")
    fun updatePerson(
        @RequestBody person: Person
    ): ResponseEntity<Person> {
        val updatedPerson = dbService.updatePerson(person)
        return if (updatedPerson == null) {
            ResponseEntity.notFound().build()
        } else {
            ResponseEntity.ok(updatedPerson)
        }
    }
}
