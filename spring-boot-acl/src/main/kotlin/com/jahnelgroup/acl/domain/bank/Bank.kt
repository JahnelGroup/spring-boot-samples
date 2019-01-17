package com.jahnelgroup.acl.domain.bank

import com.jahnelgroup.acl.domain.AbstractEntity
import com.jahnelgroup.acl.domain.account.Account
import javax.persistence.*

//@Table(name = "banks")
//@Entity
data class Bank (

        var name: String,

        @field:OneToMany(mappedBy = "bank")
        var accounts: MutableSet<Account> = mutableSetOf()

): AbstractEntity()