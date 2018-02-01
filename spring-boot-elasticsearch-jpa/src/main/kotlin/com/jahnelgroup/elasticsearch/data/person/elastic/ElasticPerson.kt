package com.jahnelgroup.elasticsearch.data.person.elastic

import org.springframework.data.elasticsearch.annotations.Document
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id


@Document(indexName = "people3")
data class ElasticPerson(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long? = null,

    var firstName: String? = null,
    var lastName: String? = null
)
