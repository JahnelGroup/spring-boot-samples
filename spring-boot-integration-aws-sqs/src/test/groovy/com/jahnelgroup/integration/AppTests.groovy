package com.jahnelgroup.integration

import com.jahnelgroup.integration.message.TextMessage
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.integration.dsl.IntegrationFlow
import org.springframework.integration.dsl.IntegrationFlows
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.MessageHandler
import org.springframework.test.context.junit4.SpringRunner

import java.util.concurrent.Semaphore

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = [App.class, TestConfig.class],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AppTests {

    @Configuration
    static class TestConfig {

        @Autowired
        private MessageChannel sqsInboundPubSubChannel

        @Bean
        Semaphore testSemaphore() {
            return new Semaphore(0)
        }

        @Bean
        MessageHandler semaphoreHandler() {
            return { testSemaphore().release(1) }
        }

        @Bean
        IntegrationFlow testPubSubFlow() {
            return IntegrationFlows.from(sqsInboundPubSubChannel)
                    .handle(semaphoreHandler())
                    .get()
        }

    }

    @Autowired
    private TestRestTemplate restTemplate

    @Autowired
    private Semaphore testSemaphore

    @Before
    void drainPermits() {
        testSemaphore.drainPermits()
    }

    @Test
    void '[AWS SQS Send Receive Async] Sends HTTP REST request and receives response asynchronously'() {
        def msg = TextMessage.fromContent('Test message.')
        def responseEntity = restTemplate.postForEntity('/textMessages', msg, TextMessage.class)
        assert responseEntity.getStatusCode() == HttpStatus.ACCEPTED
        def resultMsg = responseEntity.getBody()
        assert msg.getContent() == resultMsg.getContent()
        assert resultMsg.getUuid() != null
        assert resultMsg.getSentToSQSTs() != null
        assert resultMsg.getReceivedFromSQSTs() == null
        testSemaphore.acquire(1)
        def persistedEntity = restTemplate.getForEntity(
                String.format('/textMessages/%s', resultMsg.getUuid()),
                TextMessage.class)
        assert persistedEntity.getStatusCode() == HttpStatus.OK
        def entity = persistedEntity.getBody()
        assert resultMsg.getUuid() == entity.getUuid()
        assert entity.getReceivedFromSQSTs() != null
    }

    @Test
    void '[AWS SQS Multiple Sends] Multiple POSTs generate unique UUIDs and correct timestamps'() {
        def msg = TextMessage.fromContent('Test message.')
        def responseEntity = restTemplate.postForEntity('/textMessages', msg, TextMessage.class)
        assert responseEntity.getStatusCode() == HttpStatus.ACCEPTED

        def secondMsg = TextMessage.fromContent('Test message two.')
        def secondResponseEntity = restTemplate.postForEntity('/textMessages', secondMsg, TextMessage.class)
        assert secondResponseEntity.getStatusCode() == HttpStatus.ACCEPTED
        testSemaphore.acquire(2)

        def firstMessage = restTemplate.getForEntity(
                    String.format('/textMessages/%s', responseEntity.getBody().getUuid()),
                    TextMessage.class)
                .getBody()

        def secondMessage = restTemplate.getForEntity(
                    String.format('/textMessages/%s', secondResponseEntity.getBody().getUuid()),
                    TextMessage.class)
                .getBody()

        assert firstMessage.getUuid() != secondMessage.getUuid()
        assert firstMessage.getSentToSQSTs() != secondMessage.getSentToSQSTs()
        assert firstMessage.getReceivedFromSQSTs() != secondMessage.getReceivedFromSQSTs()
    }

    @Test
    void '[AWS SQS GET 404] GET request appropriately returns 404 when not found'() {
        def response = restTemplate.getForEntity(
                String.format('/textMessages/%s', UUID.randomUUID().toString()),
                TextMessage.class)
        assert response.getStatusCode() == HttpStatus.NOT_FOUND
    }

}
