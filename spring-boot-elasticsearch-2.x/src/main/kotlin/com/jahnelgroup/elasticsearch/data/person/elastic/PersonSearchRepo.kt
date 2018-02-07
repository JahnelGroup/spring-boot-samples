package com.jahnelgroup.elasticsearch.data.person.elastic

import com.jahnelgroup.elasticsearch.data.person.Person
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

interface PersonSearchRepo : ElasticsearchRepository<ElasticPerson, Long>  {
    fun findByFirstName(name: String): List<ElasticPerson>
}
