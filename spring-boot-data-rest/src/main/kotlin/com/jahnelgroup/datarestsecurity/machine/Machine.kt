package com.jahnelgroup.datarestsecurity.machine

import com.jahnelgroup.datarestsecurity.audit.AbstractEntity
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import javax.persistence.Entity
import javax.persistence.EntityListeners

@Entity
@EntityListeners(value = AuditingEntityListener::class)
data class Machine (

    var name : String = "",
    var type : String = ""

) : AbstractEntity()