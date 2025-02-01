package net.leejjon.crud.database

import net.leejjon.crud.model.Person
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class PersonRowMapper : RowMapper<Person> {
    override fun mapRow(rs: ResultSet, rowNum: Int): Person {
        return Person(
            rs.getInt("ID"),
            rs.getString("FULL_NAME"),
            rs.getDate("DATE_OF_BIRTH").toLocalDate(),
            rs.getDouble("HEIGHT_IN_METERS")
        )
    }
}
