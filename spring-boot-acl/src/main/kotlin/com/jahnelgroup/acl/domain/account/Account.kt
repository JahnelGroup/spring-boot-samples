package com.jahnelgroup.acl.domain.account

import com.jahnelgroup.acl.domain.AbstractEntity
import com.jahnelgroup.acl.domain.user.User
import com.jahnelgroup.springframework.security.acl.annotations.Ace
import com.jahnelgroup.springframework.security.acl.annotations.AclSecured
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@AclSecured
data class Account(

        @field:NotNull
        @field:Column(nullable = false)
        var name: String? = null,

        @Ace(permissions = ["administration", "write", "read"])
        @field:NotNull
        @field:OneToOne
        var primaryOwner: User? = null,

        @Ace(permissions = ["write", "read"])
        @field:ManyToMany
        var jointOwners: MutableSet<User> = mutableSetOf(),

        @Ace(permissions = ["read"])
        @field:ManyToMany
        var readOnly: MutableSet<User> = mutableSetOf()

): AbstractEntity()