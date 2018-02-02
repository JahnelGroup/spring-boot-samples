package com.jahnelgroup.rest.data.user

import com.jahnelgroup.rest.common.data.AbstractEntity
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import javax.persistence.Entity
import javax.persistence.EntityListeners

@Entity
@EntityListeners(value = AuditingEntityListener::class)
data class User (var username: String = "") : AbstractEntity()