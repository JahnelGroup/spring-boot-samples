package com.jahnelgroup.acl.domain.bank.relationship

import com.jahnelgroup.acl.domain.AbstractEntity
import com.jahnelgroup.acl.domain.account.Account
import com.jahnelgroup.acl.domain.user.User
import com.jahnelgroup.springframework.security.acl.annotations.Ace
import com.jahnelgroup.springframework.security.acl.annotations.AclSecured
import javax.persistence.*
import javax.validation.constraints.NotNull

//@Entity
//@AclSecured
@Table(name = "bank_relationships")
data class BankRelationship (

        @field:OneToMany(mappedBy = "bankRelationship")
        var accounts: MutableSet<Account> = mutableSetOf(),

        @Ace(permissions = ["write", "read"])
        @field:ManyToMany
        var jointOwners: MutableSet<User> = mutableSetOf(),

        @Ace(permissions = ["read"])
        @field:ManyToMany
        var readOnly: MutableSet<User> = mutableSetOf()

): AbstractEntity()