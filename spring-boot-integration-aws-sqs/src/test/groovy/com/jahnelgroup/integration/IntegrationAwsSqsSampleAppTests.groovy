package com.jahnelgroup.integration

import com.amazonaws.services.sqs.AmazonSQSAsync
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.integration.aws.inbound.SqsMessageDrivenChannelAdapter
import org.springframework.integration.aws.outbound.SqsMessageHandler
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IntegrationAwsSqsSampleAppTests {

    @MockBean
    private AmazonSQSAsync amazonSQS

    @MockBean
    private SqsMessageHandler sqsMessageHandler

    @MockBean
    private SqsMessageDrivenChannelAdapter sqsMessageDrivenChannelAdapter

    @Autowired
    private MessageChannel sqsReceiveChannel

    @Autowired
    private TestRestTemplate restTemplate

    @Before
    void mockAmazonSQS() {
        BDDMockito
            .doAnswer({
                sqsReceiveChannel.send(it.getArgumentAt(0, Message.class))
            })
            .when(sqsMessageHandler).handleMessage(BDDMockito.any(Message.class))
    }

    @Test
    void '[AWS SQS Send Receive Async] Sends HTTP REST request and receives response asynchronously'() {
        def msg = TextMessage.fromContent("Test message.")
        def responseEntity = restTemplate.postForEntity("/textMessages", msg, TextMessage.class)
        assert responseEntity.getStatusCode() == HttpStatus.ACCEPTED
        def resultMsg = responseEntity.getBody()
        assert msg.getContent() == resultMsg.getContent()
        assert resultMsg.getUuid() != null
        assert resultMsg.getSentTs() != null
        assert resultMsg.getReceivedTs() != null
    }

}
