package com.jahnelgroup.acl

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class LukoilApplication

fun main(args: Array<String>) {
	runApplication<LukoilApplication>(*args)
}

