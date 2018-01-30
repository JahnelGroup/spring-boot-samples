package com.jahnelgroup.integration.message

import lombok.Data

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import java.io.Serializable
import java.time.Duration
import java.time.LocalDateTime

@Entity
@Table(name = "textMessages")
@Data
class TextMessage : Serializable {

    companion object {

        fun fromContent(content: String): TextMessage {
            val message = TextMessage()
            message.content = content
            return message
        }
    }

    @Id
    var uuid: String = ""
    var content: String? = ""
    var sentToSQSTs: LocalDateTime? = null
    var receivedFromSQSTs: LocalDateTime? = null

    fun computeSendReceiveDiff(): Long = Duration.between(sentToSQSTs, receivedFromSQSTs).toMillis()

}
