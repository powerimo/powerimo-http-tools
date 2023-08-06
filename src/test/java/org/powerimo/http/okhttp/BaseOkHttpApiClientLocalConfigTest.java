package org.powerimo.http.okhttp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BaseOkHttpApiClientLocalConfigTest {

    @Test
    void builderTest() {
        var config = BaseOkHttpApiClientLocalConfig.builder()
                .apiKey("123")
                .url("http://localhost")
                .build();

        Assertions.assertNotNull(config);
        Assertions.assertEquals("http://localhost", config.getUrl());
        Assertions.assertEquals("123", config.getApiKey());

    }
}
