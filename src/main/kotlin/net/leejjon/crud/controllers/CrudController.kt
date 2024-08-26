package net.leejjon.crud.controllers

import net.leejjon.crud.jdbc.template.JdbcTemplateService
import net.leejjon.crud.model.Person
import net.leejjon.crud.model.Persons
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController("/v1")
class CrudController(
    private val jdbcTemplateService: JdbcTemplateService,
) {
    @GetMapping("/v1/persons")
    fun listPersons(): ResponseEntity<Persons> {
        return ResponseEntity.ok(Persons(jdbcTemplateService.getPersonsFromDb()))
    }
}
