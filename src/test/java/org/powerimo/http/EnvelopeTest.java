package org.powerimo.http;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class EnvelopeTest {

    @Test
    void testEnvelopeBuilder() {

        Envelope<String> envelope = Envelope.builderByClass(String.class)
                .code(200)
                .message("success")
                .data("test data")
                .path("/v1/location/path")
                .build();

        assertNotNull(envelope.getTimestamp());
        assertEquals(200, envelope.getCode());
        assertEquals("success", envelope.getMessage());
        assertEquals("test data", envelope.getData());
        assertEquals("/v1/location/path", envelope.getPath());
    }

    @Test
    void testEnvelopeOf() {
        Object payload = "test payload";
        Envelope<?> envelope = Envelope.of(payload);

        assertNotNull(envelope.getTimestamp());
        assertEquals(200, envelope.getCode());
        assertEquals(payload, envelope.getData());
    }

    @Test
    void testEnvelopeOfWithRequest() {
        Object payload = "test payload";
        var path = "/api/v1/test";

        Envelope<?> envelope = Envelope.of(payload, path);

        assertNotNull(envelope.getTimestamp());
        assertEquals(200, envelope.getCode());
        assertEquals(payload, envelope.getData());
        assertEquals("/api/v1/test", envelope.getPath());
    }

    @Test
    void testEnvelopeError() {
        String payload = "test payload";
        String message = "error message";
        int code = 400;

        Envelope<String> envelope = Envelope.error(payload, code, message);

        assertNotNull(envelope.getTimestamp());
        assertEquals(code, envelope.getCode());
        assertEquals(payload, envelope.getData());
        assertEquals(message, envelope.getMessage());
    }

    @Test
    void testSeparatePath() {
        Object payload = "test payload";
        String message = "error message";
        String path = "sample path";
        Instant tm = Instant.now().minusSeconds(1000);
        String messageCode = "TEST-CODE";
        int code = 400;

        Envelope<Object> envelope = Envelope.builder()
                .path(path)
                .data(payload)
                .message(message)
                .messageCode(messageCode)
                .code(code)
                .timestamp(tm)
                .build();

        assertNotNull(envelope.getTimestamp());
        assertEquals(code, envelope.getCode());
        assertEquals(payload, envelope.getData());
        assertEquals(message, envelope.getMessage());
        assertEquals(path, envelope.getPath());
        assertEquals(tm, envelope.getTimestamp());
        assertEquals(messageCode, envelope.getMessageCode());
    }
}
