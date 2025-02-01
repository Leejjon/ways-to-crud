package net.leejjon.crud.database

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import net.leejjon.crud.model.NewPerson
import net.leejjon.crud.model.Person
import java.time.LocalDate

@Entity(name = "PERSON")
class PersonEntity(
    var fullName: String,
    var dateOfBirth: LocalDate,
    var heightInMeters: Double
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null
}

fun NewPerson.toPersonEntity() = PersonEntity(
    this.name,
    this.dateOfBirth,
    this.heightInMeters
)

fun Person.toPersonEntity() = PersonEntity(
    this.name,
    this.dateOfBirth,
    this.heightInMeters
)

fun PersonEntity.toPerson(): Person = Person(
    this.id!!,
    this.fullName,
    this.dateOfBirth,
    this.heightInMeters
)
