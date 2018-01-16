package com.jahnelgroup.rest.common.time

import java.time.ZonedDateTime

interface DateTimeService {

    fun getCurrentDateAndTime() : ZonedDateTime

}