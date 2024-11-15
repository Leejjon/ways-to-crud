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
class DatabaseService(
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
            throw ResponseStatusException(HttpStatusCode.valueOf(500));
        }
    }

}
