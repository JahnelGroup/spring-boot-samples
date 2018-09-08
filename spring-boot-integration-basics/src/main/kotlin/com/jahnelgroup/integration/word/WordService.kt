package com.jahnelgroup.integration.word

import org.springframework.integration.annotation.MessageEndpoint
import org.springframework.integration.annotation.ServiceActivator

@MessageEndpoint
class WordService {

    @ServiceActivator(inputChannel = "wordChannelResult")
    fun recv(result: Int){
        println("Received: $result")
    }

}