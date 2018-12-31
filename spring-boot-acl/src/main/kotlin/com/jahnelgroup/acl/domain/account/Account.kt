package com.jahnelgroup.acl.domain.account

import com.jahnelgroup.acl.domain.AbstractEntity
import com.jahnelgroup.acl.domain.user.User
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.OneToMany
import javax.persistence.OneToOne
import javax.validation.constraints.NotNull

@Entity
data class Account(

        @field:NotNull
        @field:Column(nullable = false)
        var name: String? = null,

        @field:NotNull
        @field:OneToOne
        var primaryOwner: User? = null,

        @field:NotNull
        @field:OneToMany
        var jointOwners: Set<User> = emptySet(),

        @field:NotNull
        @field:OneToMany
        var readOnly: Set<User> = emptySet()

): AbstractEntity()