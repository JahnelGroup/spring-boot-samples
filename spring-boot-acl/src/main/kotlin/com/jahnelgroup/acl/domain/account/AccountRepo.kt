package com.jahnelgroup.acl.domain.account

import org.springframework.data.jpa.repository.JpaRepository

interface AccountRepo : JpaRepository<Account, Long>