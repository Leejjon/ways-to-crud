package net.leejjon.crud.database

import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.transaction.Transactional
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Repository
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate

@Repository
class PersonCriteriaRepository(
    @PersistenceContext
    val em: EntityManager
) {
    @Transactional
    fun updatePersonAttributes(personId: Int, updatedFields: Map<String, Any?>) {
        val cb = em.criteriaBuilder
        val cu = cb.createCriteriaUpdate(PersonEntity::class.java)
        val person = cu.from(PersonEntity::class.java)
        for (fieldToUpdate in updatedFields.entries.iterator()) {
            val castedFieldToUpdateValue = when (fieldToUpdate.key) {
                "name" -> fieldToUpdate.value as String
                "dateOfBirth" -> fieldToUpdate.value as LocalDate
                "heightInMeters" -> fieldToUpdate.value as Double
                else -> throw ResponseStatusException(HttpStatusCode.valueOf(400))
            }

            cu.set(fieldToUpdate.key, castedFieldToUpdateValue)
        }
        cu.where(cb.equal(person.get<Int>("id"), personId))

        em.createQuery(cu).executeUpdate()
    }
}
