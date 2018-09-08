package com.jahnelgroup.integration

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
class WordIntegrationTests {

    @Autowired
    lateinit var wordGateway: WordGateway

    //@MockBean
    //lateinit var wordService: WordService

    @Captor
    lateinit var intCaptor: ArgumentCaptor<Int>

    @Test
    fun wordCount(){
        wordGateway.wordCount("I really like Spring!")
      //  verify(wordService, times(1)).recv(intCaptor.capture())
        //assertThat(intCaptor.value).isEqualTo(18)
    }

    @Test
    fun resequence(){
        wordGateway.resequence("ABC")

    }

}