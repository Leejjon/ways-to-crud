package net.leejjon.crud.jdbc.template

import net.leejjon.crud.model.Person
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service

@Service
class JdbcTemplateService(
    val jdbcTemplate: JdbcTemplate
) {
    fun getPersonsFromDb(): List<Person> {
        return jdbcTemplate.query("SELECT * FROM PERSON", PersonRowMapper())
    }
}
