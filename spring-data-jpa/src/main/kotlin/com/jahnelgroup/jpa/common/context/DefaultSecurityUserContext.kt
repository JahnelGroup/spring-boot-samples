package com.jahnelgroup.rest.common.context

import com.jahnelgroup.rest.data.user.User
import com.jahnelgroup.rest.data.user.UserRepo
import org.springframework.stereotype.Component

@Component
class DefaultSecurityUserContext(private var userRepo: UserRepo): UserContextService {

    override fun getCurrentUser(): User? {
        var default = User("user")
        default.id = 1
        return default
    }

    override fun getCurrentUserId(): Long? = 1

    override fun getCurrentUsername() : String = "user"

}