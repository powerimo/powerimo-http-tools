package org.powerimo.http.okhttp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import okhttp3.MediaType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.powerimo.common.utils.Utils;
import org.powerimo.http.MockDataObject;
import org.powerimo.http.exceptions.PayloadConvertException;

import java.io.IOException;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DefaultPayloadConverterTest {
    private final DefaultPayloadConverter converter = new DefaultPayloadConverter();

    @Data
    public static class TestObject {
        private int intField = 10;
        private String stringField = "abc";
    }

    @Test
    void serializeTest() {
        var data = converter.serialize(new TestObject());

        assertNotNull(data);
        assertEquals(MediaType.get("application/json"), data.contentType());
    }

    @Test
    void deserializeTest() throws IOException {
        var s = Utils.readTextResource("response1.json");

        var data = converter.deserialize(s, TestObject.class);
        assertNotNull(data);
        assertEquals(123, data.intField);
        assertEquals("sample string field", data.stringField);
    }

    @Test
    void constructorWithObjectMapperTest() {
        var mapper = new ObjectMapper();
        var converter = new DefaultPayloadConverter(mapper);

        assertNotNull(converter);
    }

    @Test
    void deserializeExceptionTest() {
        var converter = new DefaultPayloadConverter();

        Assertions.assertThrows(PayloadConvertException.class, () -> converter.deserialize("{aaa", MockDataObject.class));
    }

    @Test
    void mapperInstantTest() throws JsonProcessingException {
        Instant t = Instant.now();
        String data = converter.getObjectMapper().writeValueAsString(t);
        assertNotNull(data);

        var t2 = converter.getObjectMapper().readValue(data, Instant.class);
        assertEquals(t, t2);
    }
}
