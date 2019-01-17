package com.jahnelgroup.acl.domain.account

import com.jahnelgroup.acl.domain.AbstractEntity
import com.jahnelgroup.acl.domain.user.User
import com.jahnelgroup.springframework.security.acl.annotations.AclAce
import com.jahnelgroup.springframework.security.acl.annotations.AclSecured
import com.jahnelgroup.springframework.security.acl.annotations.AclSid
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@AclSecured
@Table(name = "accounts")
data class Account(

        @field:NotNull
        @field:Column(nullable = false)
        var name: String? = null,

        @AclAce(permissions = ["administration", "write", "read"])
        @field:NotNull
        @field:OneToOne
        var primaryOwner: User? = null,

        @AclAce(permissions = ["write", "read"], sid = AclSid(principal = false))
        @field:ManyToMany
        var jointOwners: MutableSet<User> = mutableSetOf(),

        @AclAce(permissions = ["read"])
        @field:ManyToMany
        var readOnly: MutableSet<User> = mutableSetOf()

//        @field:NotNull
//        @field:ManyToOne
//        var bankRelationship: BankRelationship? = null

): AbstractEntity()