package net.leejjon.crud.model

import java.time.LocalDate

data class Person(
    val id: Int,
    val name: String,
    val dateOfBirth: LocalDate,
    val heightInMeters: Double
)
