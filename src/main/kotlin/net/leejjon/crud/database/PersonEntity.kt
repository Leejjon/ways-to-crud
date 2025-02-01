package net.leejjon.crud.database

import net.leejjon.crud.model.Person
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.date

object PersonEntity : IntIdTable("PERSON") {
    val name = varchar("FIRST_NAME", 50)
    val dateOfBirth = date("DATE_OF_BIRTH")
    val heightInMeters = double("HEIGHT_IN_METERS")
}

fun ResultRow.toPerson(): Person = Person(
    id = this[PersonEntity.id].value,
    name = this[PersonEntity.name],
    dateOfBirth = this[PersonEntity.dateOfBirth],
    heightInMeters = this[PersonEntity.heightInMeters]
)
