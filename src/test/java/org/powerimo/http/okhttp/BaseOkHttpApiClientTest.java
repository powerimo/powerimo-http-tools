package org.powerimo.http.okhttp;

import okhttp3.Request;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.powerimo.common.utils.Utils;
import org.powerimo.http.MockDataObject;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.spy;

public class BaseOkHttpApiClientTest {
    private static final String TEST_URL = "http://localhost:8080";

    private BaseOkHttpApiClient apiClient;
    public BaseOkHttpApiClientLocalConfig config;

    public MockWebServer mockWebServer;

    @BeforeEach
    public void setup() {
        config = new BaseOkHttpApiClientLocalConfig();
        config.setApiKey("ApiKeyValue");

        apiClient = new BaseOkHttpApiClient();
        apiClient.setConfig(config);
        apiClient.setPayloadConverter(new StdPayloadConverter());

        mockWebServer = new MockWebServer();
        config.setUrl("http://" + mockWebServer.getHostName() + ":" + mockWebServer.getPort());
    }

    @Test
    void buildUrlTest() {
        var url = apiClient.buildUrl("v1/account");

        assertEquals(config.getUrl() + "/v1/account", url);
    }

    @Test
    void extractBodyEmpty() throws IOException {
        var mockClient = spy(apiClient);
        var answerBody = Utils.readTextResource("response2.json");

        mockWebServer.enqueue(new MockResponse()
                .setBody(answerBody));

        Request request = new Request.Builder()
                .url(mockWebServer.url("/"))
                .build();
        var response = mockClient.executeRequest(request);
        assertNotNull(response);
        var payload = apiClient.extractBody(MockDataObject.class, response, true);
        assertNotNull(payload);
        assertEquals(123, payload.getIntField());
        assertEquals("sample string field", payload.getStringField());
    }

    @Test
    void testGet() throws IOException {
        var answerBody = Utils.readTextResource("response2.json");

        mockWebServer.enqueue(new MockResponse()
                .setBody(answerBody));

        var url = apiClient.buildUrl("test");
        var data = apiClient.executeGet(url, MockDataObject.class);
        assertNotNull(data);
    }

}
