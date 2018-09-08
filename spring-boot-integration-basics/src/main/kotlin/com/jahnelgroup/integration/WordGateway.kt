package com.jahnelgroup.integration

import org.springframework.integration.annotation.Gateway
import org.springframework.integration.annotation.MessagingGateway

@MessagingGateway
interface WordGateway {

    @Gateway(requestChannel = "wordChannel")
    fun wordCount(msg: String)

    @Gateway(requestChannel = "resequenceChannel")
    fun resequence(msg: String)

}