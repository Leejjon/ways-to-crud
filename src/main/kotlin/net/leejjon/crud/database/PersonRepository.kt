package net.leejjon.crud.database

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PersonRepository : CrudRepository<PersonEntity, Int> {
//    @Query("")
//    fun updatePlayer()
}
