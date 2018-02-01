package com.jahnelgroup.elasticsearch.data

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.domain.AbstractAggregateRoot
import org.springframework.hateoas.Identifiable
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class AbstractEntity : AbstractAggregateRoot(), Identifiable<Long> {

    companion object {
        val serialVersionUId = 1L
    }

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id : Long? = null

    override fun getId(): Long? {
        return id
    }

    fun setId(id: Long?) {
        this.id = id
    }

}
