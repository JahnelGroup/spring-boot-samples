package com.jahnelgroup.session.hazelcast

import io.prometheus.client.spring.boot.EnablePrometheusEndpoint
import io.prometheus.client.spring.boot.EnableSpringBootMetricsCollector
import io.prometheus.client.spring.web.EnablePrometheusTiming
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal
import javax.servlet.http.HttpSession

@RestController
@SpringBootApplication
@EnablePrometheusTiming
@EnablePrometheusEndpoint
@EnableSpringBootMetricsCollector
class UserApplication {

    @GetMapping("/api/unprotected")
    fun unprotected(session : HttpSession, p : Principal?, @Value("\${server.port}") port : Int) =
        hashMapOf(
            Pair("sessionId", session.id),
            Pair("user", p?.name),
            Pair("port", port),
            Pair("message", "Anyone can see this.")
        )

    @GetMapping("/api/protected")
    fun protected(session : HttpSession, p : Principal, @Value("\${server.port}") port : Int) =
        hashMapOf(
            Pair("sessionId", session.id),
            Pair("user", p.name),
            Pair("port", port),
            Pair("message", "You are logged in so you can see this.")
        )
}

fun main(args: Array<String>) {
    SpringApplication.run(UserApplication::class.java, *args)
}