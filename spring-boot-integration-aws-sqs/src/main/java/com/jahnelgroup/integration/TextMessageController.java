package com.jahnelgroup.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(path = "/textMessages")
public class TextMessageController {

    @Autowired
    private IntegrationAwsSqsSampleApp.SqsGateway sqsGateway;

    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.ACCEPTED)
    public TextMessage sendTextMessage(@RequestBody TextMessage textMessage) throws InterruptedException, ExecutionException {
        Assert.hasText(textMessage.getContent(), "Must supply content of the TextMessage.");
        return sqsGateway.sendMessage(textMessage);
    }

    @GetMapping("/{uuid}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public TextMessage getMessage(@PathVariable("uuid") String uuid) {
        return sqsGateway.getMessage(uuid);
    }

}
