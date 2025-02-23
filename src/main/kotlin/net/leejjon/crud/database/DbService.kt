package net.leejjon.crud.database

import io.github.oshai.kotlinlogging.KotlinLogging
import net.leejjon.crud.model.NewPerson
import net.leejjon.crud.model.Person
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired

import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate

import net.leejjon.crud.database.model.tables.Person as PersonTable

@Service
class DbService {
    private val logger = KotlinLogging.logger {}

    @Autowired
    lateinit var dsl: DSLContext

    fun getPersonsFromDb(): List<Person> {
        val persons = dsl.select().from(PersonTable.PERSON).fetch()
        return persons.map {
            Person(
                it.get(PersonTable.PERSON.ID),
                it.get(PersonTable.PERSON.FULL_NAME),
                it.get(PersonTable.PERSON.DATE_OF_BIRTH),
                it.get(PersonTable.PERSON.HEIGHT_IN_METERS)
            )
        }
    }

    fun getPerson(id: Int): Person? {
        val person = dsl.select().from(PersonTable.PERSON)
            .where(PersonTable.PERSON.ID.eq(id))
            .fetchOne()
        return if (person != null) Person(
            person.get(PersonTable.PERSON.ID),
            person.get(PersonTable.PERSON.FULL_NAME),
            person.get(PersonTable.PERSON.DATE_OF_BIRTH),
            person.get(PersonTable.PERSON.HEIGHT_IN_METERS)
        ) else null
    }

    @Transactional
    fun createPerson(person: NewPerson): Person? {
        val createdPerson = dsl.insertInto(PersonTable.PERSON)
            .set(PersonTable.PERSON.FULL_NAME, person.fullName)
            .set(PersonTable.PERSON.DATE_OF_BIRTH, person.dateOfBirth)
            .set(PersonTable.PERSON.HEIGHT_IN_METERS, person.heightInMeters)
            .returningResult(PersonTable.PERSON.ID)
            .fetchOne()
        if (createdPerson != null) {
            val createdPersonId = createdPerson.into(Integer::class.java)
            return getPerson(createdPersonId.toInt())
        } else {
            logger.error { "Unable to create person $person" }
            throw ResponseStatusException(HttpStatusCode.valueOf(500))
        }
    }

    fun deletePerson(id: Int) {
        val deletedRecord = dsl.delete(PersonTable.PERSON).where(PersonTable.PERSON.ID.eq(id)).execute()

        if (deletedRecord == 0) {
            logger.error { "Could not find person with id $id" }
            throw ResponseStatusException(HttpStatusCode.valueOf(404))
        }
    }

    @Transactional
    fun updatePerson(person: Person): Person {
        val updatedRecords = dsl.update(PersonTable.PERSON)
            .set(PersonTable.PERSON.FULL_NAME, person.fullName)
            .set(PersonTable.PERSON.DATE_OF_BIRTH, person.dateOfBirth)
            .set(PersonTable.PERSON.HEIGHT_IN_METERS, person.heightInMeters)
            .where(PersonTable.PERSON.ID.eq(person.id))
            .execute()
        if (updatedRecords == 1) {
            return getPerson(person.id)!!
        } else {
            logger.error { "Could not find person with id ${person.id}" }
            throw ResponseStatusException(HttpStatusCode.valueOf(404))
        }
    }

    @Transactional
    fun updatePersonAttributes(personId: Int, updatedFields: Map<String, Any?>): Person {
        // Stack overflow told me to use the UpdateSetStep.set(Record) function
        // https://stackoverflow.com/questions/42923028/jooq-dynamic-set
        val recordWithWhereStatements = dsl.newRecord(PersonTable.PERSON)

        for (fieldToUpdate in updatedFields.entries.iterator()) {
            when (fieldToUpdate.key) {
                "fullName" -> recordWithWhereStatements.set(PersonTable.PERSON.FULL_NAME, fieldToUpdate.value as String)
                "dateOfBirth" -> recordWithWhereStatements.set(
                    PersonTable.PERSON.DATE_OF_BIRTH,
                    LocalDate.parse(fieldToUpdate.value as String)
                )

                "heightInMeters" -> recordWithWhereStatements.set(
                    PersonTable.PERSON.HEIGHT_IN_METERS,
                    fieldToUpdate.value as Double
                )

                else -> throw ResponseStatusException(HttpStatusCode.valueOf(400))
            }
        }

        val updatedRecords =
            dsl.update(PersonTable.PERSON)
                .set(recordWithWhereStatements)
                .where(PersonTable.PERSON.ID.eq(personId))
                .execute()
        if (updatedRecords == 1) {
            return getPerson(personId)!!
        } else {
            logger.error { "Could not find person with id ${personId}" }
            throw ResponseStatusException(HttpStatusCode.valueOf(404))
        }
    }
}
