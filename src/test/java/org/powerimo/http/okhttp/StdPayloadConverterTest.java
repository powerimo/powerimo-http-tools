package org.powerimo.http.okhttp;

import lombok.Data;
import okhttp3.MediaType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.powerimo.common.utils.Utils;

import java.io.IOException;

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

        Assertions.assertNotNull(data);
        Assertions.assertEquals(MediaType.get("application/json"), data.contentType());
    }

    @Test
    void deserializeTest() throws IOException {
        var s = Utils.readTextResource("response1.json");

        var data = converter.convert(s, TestObject.class);
        Assertions.assertNotNull(data);
        Assertions.assertEquals(123, data.intField);
        Assertions.assertEquals("sample string field", data.stringField);
    }

    @Test
    void deserializeEnvelope() throws IOException {
        var s = Utils.readTextResource("response2.json");

        var envelope = converter.convertEnvelope(s, TestObject.class);
        Assertions.assertNotNull(envelope);

        var data = envelope.getData();
        Assertions.assertNotNull(data);
        Assertions.assertEquals(123, data.intField);
        Assertions.assertEquals("sample string field", data.stringField);
    }

}
