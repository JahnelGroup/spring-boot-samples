package com.jahnelgroup.acl.service.context

import java.time.Instant

interface DateTimeService {
    fun getInstant(): Instant
}