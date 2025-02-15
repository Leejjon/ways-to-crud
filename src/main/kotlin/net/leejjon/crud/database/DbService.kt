package net.leejjon.crud.database

import io.github.oshai.kotlinlogging.KotlinLogging
import net.leejjon.crud.model.NewPerson
import net.leejjon.crud.model.Person

import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class DbService {
    private val logger = KotlinLogging.logger {}
    fun getPersonsFromDb(): List<Person> = emptyList()

    fun getPerson(id: Int): Person? = null

    fun createPerson(person: NewPerson): Person? = null

    fun deletePerson(id: Int) {
        logger.error { "Could not find person with id $id" }
        throw ResponseStatusException(HttpStatusCode.valueOf(404))
    }

    fun updatePerson(person: Person): Person {
        logger.error { "Could not find person with id ${person.id}" }
        throw ResponseStatusException(HttpStatusCode.valueOf(404))
    }

    fun updatePersonAttributes(personId: Int, updatedFields: Map<String, Any?>): Person {
        logger.error { "Could not find person with id ${personId}" }
        throw ResponseStatusException(HttpStatusCode.valueOf(404))
    }
}
