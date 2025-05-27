package org.powerimo.http;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.powerimo.common.utils.Utils;
import org.powerimo.http.keycloak.payloads.IntrospectResponsePayload;

import java.io.IOException;

public class DeserializeTest {
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    }

    @Test
    public void testAudArray() throws IOException {
        var body = Utils.readTextResource("keycloak/introspect_success_3.json");
        var data = mapper.readValue(body, IntrospectResponsePayload.class);
        Assertions.assertNotNull(data);
        Assertions.assertNotNull(data.getAud());
        Assertions.assertEquals(2, data.getAud().size());
    }

    @Test
    public void testAudString() throws IOException {
        var body = Utils.readTextResource("keycloak/introspect_success.json");
        var data = mapper.readValue(body, IntrospectResponsePayload.class);
        Assertions.assertNotNull(data);
        Assertions.assertNotNull(data.getAud());
        Assertions.assertEquals(1, data.getAud().size());
    }
}
