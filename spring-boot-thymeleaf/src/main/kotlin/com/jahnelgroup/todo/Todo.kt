package com.jahnelgroup.todo

import org.springframework.data.repository.CrudRepository
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "todolist")
data class Todo (
    @field:Id
    @field:GeneratedValue()
    var id: Int? = null,
    var todoTitle: String? = null,
    var todoDescription: String? = null,
    var date: Date? = null
)

interface TodoRepo : CrudRepository<Todo, Int>