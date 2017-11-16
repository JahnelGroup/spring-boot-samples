package com.jahnelgroup.datarestsecurity.supplier

import com.jahnelgroup.datarestsecurity.audit.AbstractEntity
import com.jahnelgroup.datarestsecurity.machine.Machine
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.OneToMany

@Entity
@EntityListeners(value = AuditingEntityListener::class)
data class Supplier(

    var name : String = ""

) : AbstractEntity(){

    @OneToMany
    var machines : List<Machine>? = null

}