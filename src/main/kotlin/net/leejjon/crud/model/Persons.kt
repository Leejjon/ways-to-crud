package net.leejjon.crud.model

import java.time.LocalDate

data class Persons(
    val persons: List<Person>
)

data class Person(
    val id: Int,
    val fullName: String?,
    val dateOfBirth: LocalDate?,
    val heightInMeters: Double?
)

data class NewPerson(
    // No ID because that cannot be changed as ID's are generated by the db.
    val fullName: String?,
    val dateOfBirth: LocalDate?,
    val heightInMeters: Double?
)
