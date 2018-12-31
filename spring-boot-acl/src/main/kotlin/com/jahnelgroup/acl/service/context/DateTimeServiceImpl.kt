package com.jahnelgroup.acl.service.context

import org.springframework.stereotype.Component
import java.time.Instant

@Component
class DateTimeServiceImpl : DateTimeService {
    override fun getInstant() = Instant.now()

}