package com.jahnelgroup.elasticsearch.common

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class CommonApplication

fun main(args: Array<String>) {
    SpringApplication.run(CommonApplication::class.java, *args)
}
