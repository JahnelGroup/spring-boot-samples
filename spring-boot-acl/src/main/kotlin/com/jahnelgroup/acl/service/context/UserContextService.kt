package com.jahnelgroup.acl.service.context

import com.jahnelgroup.acl.domain.user.User

interface UserContextService {

    fun impersonateUser(user: User)

    fun currentUser(): User
    fun currentUsername(): String
    fun currentAuthorities(): Set<String>

}