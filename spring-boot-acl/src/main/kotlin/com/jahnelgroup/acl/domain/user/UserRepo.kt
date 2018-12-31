package com.jahnelgroup.acl.domain.user

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepo : JpaRepository<User, Long> {

    fun findByUsername(username: String): Optional<User>

}
