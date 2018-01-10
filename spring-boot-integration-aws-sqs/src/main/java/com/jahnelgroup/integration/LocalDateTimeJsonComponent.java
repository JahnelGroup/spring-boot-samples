package com.jahnelgroup.integration;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@JsonComponent
public class LocalDateTimeJsonComponent {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    public static class LocalDateTimeDeserializer extends FromStringDeserializer<LocalDateTime> {

        public LocalDateTimeDeserializer() {
            super(LocalDateTime.class);
        }

        @Override
        public LocalDateTime _deserialize(String value, DeserializationContext ctxt) throws IOException {
            return LocalDateTime.from(FORMATTER.parse(value));
        }

    }

    public static class LocalDateTimeSerializer extends StdSerializer<LocalDateTime> {

        public LocalDateTimeSerializer() {
            super(LocalDateTime.class);
        }

        @Override
        public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            String dateString = null;
            if(value!=null) {
                dateString = FORMATTER.format(value);
            }
            gen.writeString(dateString);
        }

    }

}
