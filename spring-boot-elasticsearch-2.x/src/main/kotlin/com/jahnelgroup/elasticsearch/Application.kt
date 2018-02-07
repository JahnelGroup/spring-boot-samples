package com.jahnelgroup.elasticsearch

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.integration.annotation.IntegrationComponentScan
import org.springframework.integration.config.EnableIntegration

@SpringBootApplication
@EnableElasticsearchRepositories(includeFilters = [(ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,  classes = [ElasticsearchRepository::class]))])
@EnableJpaRepositories(includeFilters = [(ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = [JpaRepository::class]))])
@EnableIntegration
@IntegrationComponentScan
open class Application

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}
