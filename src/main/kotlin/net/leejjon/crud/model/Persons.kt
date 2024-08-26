package net.leejjon.crud.model

import java.time.LocalDate

data class Persons(
    val persons: List<Person>
)

data class Person(
    val id: Int,
    val name: String,
    val dateOfBirth: LocalDate,
    val heightInMeters: Double
)
