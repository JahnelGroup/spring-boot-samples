package com.jahnelgroup.rest.common.context

import com.jahnelgroup.rest.data.user.User

interface UserContextService {

    fun getCurrentUser() : User?
    fun getCurrentUserId() : Long?
    fun getCurrentUsername() : String?

}