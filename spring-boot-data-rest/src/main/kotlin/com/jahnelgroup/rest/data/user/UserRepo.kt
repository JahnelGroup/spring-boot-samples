package com.jahnelgroup.rest.data.user

import org.springframework.data.repository.CrudRepository
import java.util.*

interface UserRepo : CrudRepository<User, Long>{

    fun findByUsername(username : String) : Optional<User>

}