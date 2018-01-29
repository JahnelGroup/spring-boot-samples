package com.jahnelgroup.rest.security

import com.jahnelgroup.rest.data.user.UserRepo
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class AuthUserDetailsService(
    val userRepo : UserRepo
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails =
        userRepo.findByUsername(username).map {
            org.springframework.security.core.userdetails.User(it.username, "password", emptySet())
        }.orElseThrow({ UsernameNotFoundException("Username not found") })
}