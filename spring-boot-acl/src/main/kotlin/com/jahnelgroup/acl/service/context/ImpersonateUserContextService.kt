package com.jahnelgroup.acl.service.context

import com.jahnelgroup.acl.domain.user.User
import com.jahnelgroup.acl.domain.user.UserRepo
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.lang.RuntimeException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Component
class ImpersonateUserContextService(private val userRepo: UserRepo) : UserContextService {

    override fun impersonateUser(user: User) {
        var u = org.springframework.security.core.userdetails.User(user.username, user.password, user.authorities.asSequence().map {
            GrantedAuthority { it.id!!.authority }
        }.toList())
        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(u.username, u.password, u.authorities)
    }

    override fun currentUsername(): String {
        val username = SecurityContextHolder.getContext().authentication.name
        return username ?: throw RuntimeException("UnauthenticatedException")
    }

    override fun currentUser(): User =
            userRepo.findByUsername(currentUsername()).get()

    override fun currentAuthorities(): Set<String> {
        var user = SecurityContextHolder.getContext().authentication.principal as
                org.springframework.security.core.userdetails.User
        return user.authorities.map{ it.authority }.toSet()
    }

}