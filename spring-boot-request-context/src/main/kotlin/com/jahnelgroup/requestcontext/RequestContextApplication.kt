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
class RequestContextApplication{
	private val logger = LoggerFactory.getLogger(javaClass)

	/**
	 * End-point to demonstrate how the MDC is printed with the requestId provided by
	 * the requestContextFilter below.
	 */
	@GetMapping("/hello")
	fun hello(@RequestParam name: String): String {
		var resp = "Hello $name"

		// This will print a log statement with the MDC, example:
		// 		13:50:15.999 [http-nio-8080-exec-9] [request-id=FAA61773FC7C4DFD8C11832F6A1BFD29] INFO  c.j.r.RequestContextApplication$$EnhancerBySpringCGLIB$$a132cd9a - Echoing back 'Hello Steven'
		logger.info("Echoing back '$resp'")

		return resp
	}

	/**
	 * Registers a request filter to generate
	 */
	@Bean
	fun customRequestContextFilter() = object : OncePerRequestFilter() {
		var tokenName = "request-id"
		var token: String? = null
		override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
			try{
				// Request ID
				token = if( !StringUtils.isEmpty(request.getHeader(tokenName)) ){
					request.getHeader(tokenName)
				}else{
					UUID.randomUUID().toString().toUpperCase().replace("-","")
				}
				MDC.put(tokenName, token)
				response.addHeader(tokenName, token)
				filterChain.doFilter(request, response)
			} finally {
				MDC.remove(tokenName)
			}
		}
	}
}

fun main(args: Array<String>) {
	runApplication<RequestContextApplication>(*args)
}