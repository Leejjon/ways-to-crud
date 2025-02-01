package net.leejjon.crud.database

import io.github.oshai.kotlinlogging.KotlinLogging
import net.leejjon.crud.model.NewPerson
import net.leejjon.crud.model.Person
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

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

//    fun updatePerson(person: Person): Person {
//        val update = try {
//            jdbcClient.sql("UPDATE PERSON SET name = :name, dateOfBirth = :dateOfBirth, heightInMeters = :heightInMeters WHERE id = :id")
//                .params(person.name, person.dateOfBirth, person.heightInMeters, person.id)
//                .update()
//        } catch (e: Exception) {
//            logger.error(e) { "Unable to delete person due to error with the query or connection" }
//            throw ResponseStatusException(HttpStatusCode.valueOf(500))
//        }
//        if (update == 1) {
//            val personFromDb = getPerson(person.id)
//            if (personFromDb.isPresent) {
//                return personFromDb.get()
//            } else {
//                logger.error { "Could not update person with id $person.id" }
//                throw ResponseStatusException(HttpStatusCode.valueOf(404))
//            }
//        } else {
//            logger.error { "Could not update person with id $person.id" }
//            throw ResponseStatusException(HttpStatusCode.valueOf(404))
//        }
//    }
}
