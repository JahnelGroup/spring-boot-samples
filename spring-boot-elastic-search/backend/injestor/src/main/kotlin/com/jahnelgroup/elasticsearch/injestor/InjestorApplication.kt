package com.jahnelgroup.elasticsearch.injestor

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class InjestorApplication

fun main(args: Array<String>) {
    SpringApplication.run(InjestorApplication::class.java, *args)
}
