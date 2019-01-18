package com.jahnelgroup.acl.domain.user

import org.springframework.data.jpa.repository.JpaRepository

interface UserAuthorityRepo : JpaRepository<UserAuthority, Long>

