package com.jahnelgroup.elasticsearch.data.person

import com.jahnelgroup.elasticsearch.data.AbstractEntity
import java.time.LocalDate
import javax.persistence.Entity

@Entity
data class Person(
    var firstName: String? = null,
    var lastName: String? = null,
    var dob: LocalDate? = null
): AbstractEntity()
