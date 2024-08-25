package net.leejjon.crud.jdbc.template

import net.leejjon.crud.model.Person
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class PersonRowMapper : RowMapper<Person> {
    override fun mapRow(rs: ResultSet, rowNum: Int): Person {
        return Person(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getDate("dateOfBirth").toLocalDate(),
            rs.getDouble("heightInMeters")
        )
    }
}
