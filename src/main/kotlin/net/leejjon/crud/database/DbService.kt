package net.leejjon.crud.database

import io.github.oshai.kotlinlogging.KotlinLogging
import net.leejjon.crud.model.Person
import org.springframework.http.HttpStatusCode
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class DbService(
    private val jdbcClient: JdbcClient
) {
    private val logger = KotlinLogging.logger {}
    fun getPersonsFromDb(): List<Person> =
        jdbcClient.sql("SELECT * FROM PERSON")
            .query(PersonRowMapper())
            .list()

    fun getPerson(id: Int): Person? =
        jdbcClient.sql("SELECT * FROM PERSON WHERE id = :id")
            .params(id)
            .query(PersonRowMapper())
            .optional().get()

    fun createPerson(person: Person): Person? {
        val keyHolder: KeyHolder = GeneratedKeyHolder()
        val update =
            jdbcClient.sql("INSERT INTO PERSON(name, dateOfBirth, heightInMeters) VALUES (:name, :dateOfBirth, :heightInMeters)")
                .params(person.name, person.dateOfBirth, person.heightInMeters)
                .update(keyHolder)

        if (update == 1) {
            return getPerson(keyHolder.getKeyAs(Integer::class.java)?.toInt()!!)
        } else {
            logger.error { "Unable to create person $person" }
            throw ResponseStatusException(HttpStatusCode.valueOf(500))
        }
    }

    fun deletePerson(id: Int) {
        val update = try {
            jdbcClient.sql("DELETE FROM PERSON WHERE ID = :id")
                .params(id)
                .update()
        } catch (e: Exception) {
            logger.error(e) { "Unable to delete person due to error with the query or connection" }
            throw ResponseStatusException(HttpStatusCode.valueOf(500))
        }
        if (update == 1) {
            return
        } else {
            logger.error { "Could not find person with id $id" }
            throw ResponseStatusException(HttpStatusCode.valueOf(404))
        }
    }

    fun updatePerson(person: Person): Person {
        val update = try {
            jdbcClient.sql("UPDATE PERSON SET name = :name, dateOfBirth = :dateOfBirth, heightInMeters = :heightInMeters WHERE id = :id")
                .params(person.name, person.dateOfBirth, person.heightInMeters, person.id)
                .update()
        } catch (e: Exception) {
            logger.error(e) { "Unable to delete person due to error with the query or connection" }
            throw ResponseStatusException(HttpStatusCode.valueOf(500))
        }
        if (update == 1) {
            val personFromDb = getPerson(person.id)
            if (personFromDb != null) {
                return personFromDb
            } else {
                logger.error { "Could not update person with id $person.id" }
                throw ResponseStatusException(HttpStatusCode.valueOf(404))
            }
        } else {
            logger.error { "Could not update person with id $person.id" }
            throw ResponseStatusException(HttpStatusCode.valueOf(404))
        }
    }
}
