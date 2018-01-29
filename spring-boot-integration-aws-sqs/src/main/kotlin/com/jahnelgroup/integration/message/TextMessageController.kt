package com.jahnelgroup.integration.message

import com.jahnelgroup.integration.message.integration.SqsGateway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.util.Assert
import org.springframework.web.bind.annotation.*

import java.util.concurrent.ExecutionException

@RestController
@RequestMapping(path = ["/textMessages"])
class TextMessageController {

    @Autowired
    lateinit var sqsGateway: SqsGateway

    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Throws(InterruptedException::class, ExecutionException::class)
    fun sendTextMessage(@RequestBody textMessage: TextMessage): TextMessage {
        Assert.hasText(textMessage.content, "Must supply content of the TextMessage.")
        return sqsGateway.sendMessage(textMessage)
    }

    @GetMapping("/{uuid}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    fun getMessage(@PathVariable("uuid") uuid: String): TextMessage = sqsGateway.getMessage(uuid)

}
