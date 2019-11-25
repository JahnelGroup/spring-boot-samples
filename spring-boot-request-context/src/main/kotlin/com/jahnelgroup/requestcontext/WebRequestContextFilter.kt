package com.jahnelgroup.requestcontext

import org.slf4j.MDC
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Registers a request filter to handle setting webRequestId processing.
 */
@Component
class WebRequestContextFilter(private var webRequestContext: WebRequestContext) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        try{
            // Request ID
            var webRequestIdHeaderName = "request-id"
            var webRequestId = if( !StringUtils.isEmpty(request.getHeader(webRequestIdHeaderName)) ){
                request.getHeader(webRequestIdHeaderName)
            }else{
                UUID.randomUUID().toString().toUpperCase().replace("-","")
            }

            webRequestContext.setWebRequestId(webRequestId)
            response.addHeader(webRequestIdHeaderName, webRequestId)

            filterChain.doFilter(request, response)
        } finally {
            MDC.clear()
        }
    }

}