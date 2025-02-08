package net.leejjon.crud.database

import io.github.oshai.kotlinlogging.KotlinLogging
import net.leejjon.crud.model.NewPerson
import net.leejjon.crud.model.Person
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate

@Service
class DbService {
    private val logger = KotlinLogging.logger {}
    fun getPersonsFromDb(): List<Person> = transaction {
        PersonEntity.selectAll().map {
                row -> row.toPerson()
        }
    }

    fun getPerson(id: Int): Person? = transaction {
        PersonEntity.selectAll().where { PersonEntity.id.eq(id) }.firstOrNull()?.toPerson()
    }

    fun createPerson(person: NewPerson): Person? = transaction {
        PersonEntity.insert {
            it[fullName] = person.fullName
            it[dateOfBirth] = person.dateOfBirth
            it[heightInMeters] = person.heightInMeters
        }.resultedValues!!.firstOrNull()?.toPerson()
    }

    fun deletePerson(id: Int) = transaction {
        val update = PersonEntity.deleteWhere { PersonEntity.id.eq(id) }
        if (update != 1) {
            logger.error { "Could not find person with id $id" }
            throw ResponseStatusException(HttpStatusCode.valueOf(404))
        }
    }

    fun updatePerson(person: Person): Person = transaction {
        val update = PersonEntity.update({ PersonEntity.id.eq(person.id) }) {
            it[fullName] = person.fullName
            it[dateOfBirth] = person.dateOfBirth
            it[heightInMeters] = person.heightInMeters
        }

        if (update != 1) {
            logger.error { "Could not find person with id ${person.id}" }
            throw ResponseStatusException(HttpStatusCode.valueOf(404))
        } else {
            val updatedPersonFromDb = getPerson(person.id)
            if (updatedPersonFromDb != null) {
                return@transaction updatedPersonFromDb
            } else {
                logger.error { "Could not update person with id $person.id" }
                throw ResponseStatusException(HttpStatusCode.valueOf(500))
            }
        }
    }

    fun updatePersonAttributes(personId: Int, updatedFields: Map<String, Any?>): Person = transaction {
        val filterColumnAndValue = updatedFields.entries.map {
            return@map when (it.key) {
                "fullName" -> Pair(PersonEntity.fullName, it.value as String)
                "dateOfBirth" -> Pair(PersonEntity.dateOfBirth, LocalDate.parse(it.value as String))
                "heightInMeters" -> Pair(PersonEntity.heightInMeters, it.value as Double)
                else -> throw ResponseStatusException(HttpStatusCode.valueOf(400))
            }
        }

        val update = PersonEntity.update({
            PersonEntity.id.eq(personId)
        }) {
            filterColumnAndValue.forEach { (column, value) ->
                it[column as Column<Any>] = value
            }
        }

        if (update != 1) {
            logger.error { "Could not find person with id ${personId}" }
            throw ResponseStatusException(HttpStatusCode.valueOf(404))
        } else {
            val updatedPersonFromDb = getPerson(personId)
            if (updatedPersonFromDb != null) {
                return@transaction updatedPersonFromDb
            } else {
                logger.error { "Could not update person with id $personId" }
                throw ResponseStatusException(HttpStatusCode.valueOf(500))
            }
        }
    }
}
