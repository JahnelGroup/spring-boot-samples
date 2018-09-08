package com.jahnelgroup.integration

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class IntegrationApplication

fun main(args: Array<String>) {
    runApplication<IntegrationApplication>(*args)
}