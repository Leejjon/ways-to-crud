package net.leejjon.crud.jdbc.template

import net.leejjon.crud.model.Person
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Service

@Service
class DatabaseService(
    val jdbcClient: JdbcClient
) {
    fun getPersonsFromDb(): List<Person> {
        return jdbcClient.sql("SELECT * FROM PERSON")
            .query(PersonRowMapper())
            .list()
    }

    fun getPerson(id: Int): Person? {
        return jdbcClient.sql("SELECT * FROM PERSON WHERE id = :id")
            .param("id", id)
            .query(PersonRowMapper())
            .optional().get()
    }
}
