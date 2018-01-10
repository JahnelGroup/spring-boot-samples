package com.jahnelgroup.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
public class TextMessageController {

    @Autowired
    private IntegrationAwsSqsSampleApp.SqsInboundGateway sqsInboundGateway;

    @RequestMapping(path = "/textMessages", method = RequestMethod.POST)
    @ResponseBody
    @ResponseStatus(HttpStatus.ACCEPTED)
    public TextMessage sendTextMessage(@RequestBody TextMessage textMessage) throws InterruptedException, ExecutionException {
        Assert.hasText(textMessage.getContent(), "Must supply content of the TextMessage.");
        final Future<TextMessage> result = sqsInboundGateway.sendMessage(textMessage);
        return result.get();
    }

}
