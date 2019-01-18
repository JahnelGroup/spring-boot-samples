package com.jahnelgroup.acl.domain.account

import org.springframework.data.repository.CrudRepository

interface AccountRepo : CrudRepository<Account, Long>{
    override fun findAll(): List<Account>
}