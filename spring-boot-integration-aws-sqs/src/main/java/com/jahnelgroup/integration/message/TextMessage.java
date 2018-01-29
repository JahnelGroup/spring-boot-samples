package com.jahnelgroup.integration.message;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "textMessages")
@Data
public class TextMessage implements Serializable {

    @Id
    private String uuid;
    private String content;
    private LocalDateTime sentToSQSTs;
    private LocalDateTime receivedFromSQSTs;

    public Long computeSendReceiveDiff() {
        return Duration.between(sentToSQSTs, receivedFromSQSTs).toMillis();
    }

    public static TextMessage fromContent(String content) {
        final TextMessage message = new TextMessage();
        message.setContent(content);
        return message;
    }

}
