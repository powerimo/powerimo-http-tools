package org.powerimo.http.okhttp;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import okhttp3.MediaType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.powerimo.common.utils.Utils;
import org.powerimo.http.MockDataObject;
import org.powerimo.http.exceptions.PayloadConvertException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class StdPayloadConverterTest {
    private final StdPayloadConverter converter = new StdPayloadConverter();

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

        var data = converter.convert(s, TestObject.class);
        assertNotNull(data);
        assertEquals(123, data.intField);
        assertEquals("sample string field", data.stringField);
    }

    @Test
    void deserializeEnvelope() throws IOException {
        var s = Utils.readTextResource("response2.json");

        var envelope = converter.convertEnvelope(s, TestObject.class);
        assertNotNull(envelope);

        var data = envelope.getData();
        assertNotNull(data);
        assertEquals(123, data.intField);
        assertEquals("sample string field", data.stringField);
    }

    @Test
    void constructorWithObjectMapperTest() {
        var mapper = new ObjectMapper();
        var converter = new StdPayloadConverter(mapper);

        assertNotNull(converter);
    }

    @Test
    void deserializeExceptionTest() {
        var converter = new StdPayloadConverter();

        Assertions.assertThrows(PayloadConvertException.class, () -> converter.convert("{aaa", MockDataObject.class));
    }

    @Test
    void deserializeEnvelopeExceptionTest() {
        var converter = new StdPayloadConverter();

        Assertions.assertThrows(PayloadConvertException.class, () -> converter.convertEnvelope("{aaa", MockDataObject.class));
    }

}
