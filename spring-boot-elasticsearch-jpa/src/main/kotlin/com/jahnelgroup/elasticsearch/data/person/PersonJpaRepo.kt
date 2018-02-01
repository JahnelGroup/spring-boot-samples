package com.jahnelgroup.elasticsearch.data.person

import org.springframework.data.jpa.repository.JpaRepository
import javax.persistence.criteria.Predicate

interface PersonJpaRepo : JpaRepository<Person, Long> {
    fun findByLastName(lastName: String): List<Person>
}
