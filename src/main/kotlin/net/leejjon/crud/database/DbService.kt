package net.leejjon.crud.database

import net.leejjon.crud.model.NewPerson
import net.leejjon.crud.model.Person
import org.springframework.stereotype.Service
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
class DbService(
    private val personRepository: PersonRepository
) {
    fun getPersonsFromDb(): List<Person> =
        personRepository.findAll().toList().map { it.toPerson() }

    fun getPerson(id: Int): Person? =
        personRepository.findById(id).getOrNull()?.toPerson()

    fun createPerson(person: NewPerson): Person {
        return personRepository.save(person.toPersonEntity()).toPerson()
    }

    fun deletePerson(id: Int) {
        personRepository.deleteById(id)
    }

    fun updatePerson(person: Person): Person? {
        val existingPerson: Optional<PersonEntity> = personRepository.findById(person.id)
        return if (existingPerson.isPresent) {
            personRepository.save(person.toPersonEntity()).toPerson()
        } else {
            null
        }
    }
}
