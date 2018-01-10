package com.jahnelgroup.integration;

import lombok.Data;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

@Data
public class TextMessage implements Serializable {

    private String uuid;
    private String content;
    private LocalDateTime sentTs;
    private LocalDateTime receivedTs;

    public Long computeSendReceiveDiff() {
        return Duration.between(sentTs, receivedTs).toMillis();
    }

    public static TextMessage fromContent(String content) {
        final TextMessage message = new TextMessage();
        message.setContent(content);
        return message;
    }

}
