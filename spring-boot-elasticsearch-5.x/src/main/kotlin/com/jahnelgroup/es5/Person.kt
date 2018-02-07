package com.jahnelgroup.es5

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document

@Document(indexName = "people")
data class Person(
    @Id
    var id : Long = 0,
    var firstName : String = "",
    var lastName : String = ""
)