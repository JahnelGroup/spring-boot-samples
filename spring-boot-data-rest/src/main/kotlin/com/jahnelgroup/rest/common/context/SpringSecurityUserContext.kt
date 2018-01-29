package com.jahnelgroup.rest.common.context

import com.jahnelgroup.rest.data.user.User
import com.jahnelgroup.rest.data.user.UserRepo
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class SpringSecurityUserContext(private var userRepo: UserRepo): UserContextService {

    override fun getCurrentUser(): User? =
            userRepo.findByUsername(getCurrentUsername()!!).orElse(null)

    override fun getCurrentUserId(): Long? =
            userRepo.findByUsername(getCurrentUsername()!!).map { it.id }.orElse(null)

    override fun getCurrentUsername() : String? =
            SecurityContextHolder.getContext().authentication.name.orEmpty()

}