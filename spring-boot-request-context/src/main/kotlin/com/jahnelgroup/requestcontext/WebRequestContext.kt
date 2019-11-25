package com.jahnelgroup.requestcontext

import org.slf4j.MDC
import org.springframework.stereotype.Component
import org.springframework.web.context.annotation.RequestScope

/**
 * Singleton used through the duration of a single Web Request. Only register the
 * fields you need to see in log statement with the MDC.
 */
@Component
@RequestScope
class WebRequestContext {

    private var webRequestId: String? = null
    private var name: String? = null

    fun setWebRequestId(webRequestId: String){
        this.webRequestId = webRequestId
        MDC.put("webRequestId", webRequestId)
    }

    fun setName(name: String){
        this.name = name
        MDC.put("name", name)
    }

}