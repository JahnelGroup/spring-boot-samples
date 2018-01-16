package com.jahnelgroup.integration

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IntegrationAwsSqsSampleAppTests {

    @Autowired
    private TestRestTemplate restTemplate

    @Test
    void '[AWS SQS Send Receive Async] Sends HTTP REST request and receives response asynchronously'() {
        def msg = TextMessage.fromContent("Test message.")
        def responseEntity = restTemplate.postForEntity("/textMessages", msg, TextMessage.class)
        assert responseEntity.getStatusCode() == HttpStatus.ACCEPTED
        def resultMsg = responseEntity.getBody()
        assert msg.getContent() == resultMsg.getContent()
        assert resultMsg.getUuid() != null
        assert resultMsg.getSentTs() != null
        assert resultMsg.getReceivedTs() == null
        sleep(3000)
        def persistedEntity = restTemplate.getForEntity(
                String.format("/textMessages/%s", resultMsg.getUuid()),
                TextMessage.class)
        assert persistedEntity.getStatusCode() == HttpStatus.OK
        def entity = persistedEntity.getBody()
        assert resultMsg.getUuid() == entity.getUuid()
        assert entity.getReceivedTs() != null
    }

}
