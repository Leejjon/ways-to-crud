package net.leejjon.crud.database

import io.github.oshai.kotlinlogging.KotlinLogging
import net.leejjon.crud.model.NewPerson
import net.leejjon.crud.model.Person
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired

import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class DbService {
    private val logger = KotlinLogging.logger {}

    @Autowired
    lateinit var dsl: DSLContext

    fun getPersonsFromDb(): List<Person> {
        val persons = dsl.select().from(net.leejjon.crud.database.model.tables.Person.PERSON).fetch()
        return persons.map {
            Person(
                it.get(net.leejjon.crud.database.model.tables.Person.PERSON.ID),
                it.get(net.leejjon.crud.database.model.tables.Person.PERSON.FULL_NAME),
                it.get(net.leejjon.crud.database.model.tables.Person.PERSON.DATE_OF_BIRTH),
                it.get(net.leejjon.crud.database.model.tables.Person.PERSON.HEIGHT_IN_METERS)
            )
        }
    }

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
