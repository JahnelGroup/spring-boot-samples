package com.jahnelgroup.datarestsecurity

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true)
class App

fun main(args: Array<String>) {
    SpringApplication.run(App::class.java, *args)
}
