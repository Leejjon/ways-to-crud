package net.leejjon.crud.database

import io.github.oshai.kotlinlogging.KotlinLogging
import net.leejjon.crud.model.NewPerson
import net.leejjon.crud.model.Person
import org.springframework.http.HttpStatusCode
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate
import java.time.format.DateTimeParseException

@Service
class DbService(
    private val jdbcClient: JdbcClient
) {
    private val logger = KotlinLogging.logger {}
    fun getPersonsFromDb(): List<Person> =
        jdbcClient.sql("SELECT * FROM PERSON")
            .query(PersonRowMapper())
            .list()

    fun getPerson(id: Int): java.util.Optional<Person> =
        jdbcClient.sql("SELECT * FROM PERSON WHERE ID = ?")
            .params(id)
            .query(PersonRowMapper())
            .optional()

    fun createPerson(person: NewPerson): Person? {
        val keyHolder: KeyHolder = GeneratedKeyHolder()
        val update =
            jdbcClient.sql("INSERT INTO PERSON(FULL_NAME, DATE_OF_BIRTH, HEIGHT_IN_METERS) VALUES (?, ?, ?)")
                .params(person.fullName, person.dateOfBirth, person.heightInMeters)
                .update(keyHolder)

        if (update == 1) {
            val potentiallyCreatedPerson = getPerson(keyHolder.getKeyAs(Integer::class.java)?.toInt()!!)

            return if (potentiallyCreatedPerson.isPresent) {
                potentiallyCreatedPerson.get()
            } else {
                throw ResponseStatusException(HttpStatusCode.valueOf(404))
            }
        } else {
            logger.error { "Unable to create person $person" }
            throw ResponseStatusException(HttpStatusCode.valueOf(500))
        }
    }

    fun deletePerson(id: Int) {
        val update = try {
            jdbcClient.sql("DELETE FROM PERSON WHERE ID = ?")
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
            jdbcClient.sql("UPDATE PERSON SET FULL_NAME = ?, DATE_OF_BIRTH = ?, HEIGHT_IN_METERS = ? WHERE ID = ?")
                .params(person.fullName, person.dateOfBirth, person.heightInMeters, person.id)
                .update()
        } catch (e: Exception) {
            logger.error(e) { "Unable to delete person due to error with the query or connection" }
            throw ResponseStatusException(HttpStatusCode.valueOf(500))
        }
        if (update == 1) {
            val personFromDb = getPerson(person.id)
            if (personFromDb.isPresent) {
                return personFromDb.get()
            } else {
                logger.error { "Could not update person with id $person.id" }
                throw ResponseStatusException(HttpStatusCode.valueOf(404))
            }
        } else {
            logger.error { "Could not update person with id $person.id" }
            throw ResponseStatusException(HttpStatusCode.valueOf(404))
        }
    }

    fun updatePersonAttributes(personId: Int, updatedFields: Map<String, Any?>): Person {
        val person = getPerson(personId)
        if (person.isPresent) {
            val updatedPerson = person.get().copy(
                fullName = updatedFields["fullName"] as? String ?: person.get().fullName,
                dateOfBirth = parseLocalDate(updatedFields["dateOfBirth"] as? String) ?: person.get().dateOfBirth,
                heightInMeters = updatedFields["heightInMeters"] as? Double ?: person.get().heightInMeters
            )
            return updatePerson(updatedPerson)
        } else {
            logger.error { "Could not update person with id $person.id" }
            throw ResponseStatusException(HttpStatusCode.valueOf(404))
        }
    }

    private fun parseLocalDate(date: String?): LocalDate? {
        if (date == null) return null
        return try {
            LocalDate.parse(date)
        } catch (e: DateTimeParseException) {
            null
        }
    }
}
