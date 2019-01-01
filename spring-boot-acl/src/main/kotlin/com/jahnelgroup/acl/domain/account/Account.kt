package com.jahnelgroup.acl.domain.account

import com.jahnelgroup.acl.domain.AbstractEntity
import com.jahnelgroup.acl.domain.user.User
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
data class Account(

        @field:NotNull
        @field:Column(nullable = false)
        var name: String? = null,

        @field:NotNull
        @field:OneToOne
        var primaryOwner: User? = null,

        @field:ManyToMany
        var jointOwners: Set<User> = emptySet(),

        @field:ManyToMany
        var readOnly: Set<User> = emptySet()

): AbstractEntity()