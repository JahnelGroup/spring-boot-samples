package com.jahnelgroup.requestcontext

import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@SpringBootApplication
class RequestContextApplication(var webRequestContext: WebRequestContext){
	private val logger = LoggerFactory.getLogger(javaClass)

	/**
	 * End-point to demonstrate how the MDC is printed. The webRequestId is handled in the
	 * WebRequestContextFilter and the name is handled right here in this function as an example
	 * of setting context throughout the application.
	 */
	@GetMapping("/hello")
	fun hello(@RequestParam name: String): String {
		var resp = "Hello $name"

		// This is just an example of setting some type of context throughout the application.
		webRequestContext.setName(name)

		// This log statement will be enriched with webRequestId and name.
		logger.info("Echoing back '$resp'")

		return resp
	}
}

fun main(args: Array<String>) {
	runApplication<RequestContextApplication>(*args)
}
