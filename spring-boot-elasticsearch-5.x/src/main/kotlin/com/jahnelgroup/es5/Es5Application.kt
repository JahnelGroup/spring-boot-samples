package com.jahnelgroup.es5

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Es5Application : CommandLineRunner {
    override fun run(vararg args: String?) {
        println("=========== Running ============")
    }

}

fun main(args: Array<String>) {
    runApplication<Es5Application>(*args)
}
