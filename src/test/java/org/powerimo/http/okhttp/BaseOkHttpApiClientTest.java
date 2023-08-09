package org.powerimo.http.okhttp;

import okhttp3.Request;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.powerimo.common.utils.Utils;
import org.powerimo.http.MockDataObject;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
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
    void getTest() throws IOException {
        var answerBody = Utils.readTextResource("response2.json");

        mockWebServer.enqueue(new MockResponse()
                .setBody(answerBody));

        var url = apiClient.buildUrl("test");
        var data = apiClient.executeGet(url, MockDataObject.class);
        assertNotNull(data);
    }

    @Test
    void postTest() throws IOException {
        var requestBody = new MockDataObject();
        requestBody.setIntField(321);
        requestBody.setStringField("aaa");

        var answerBody = Utils.readTextResource("response2.json");
        mockWebServer.enqueue(new MockResponse()
                .setBody(answerBody));

        var url = apiClient.buildUrl("test");
        var data = apiClient.executePost(url, MockDataObject.class, requestBody);

        assertNotNull(data);
    }

    @Test
    void deleteTest() throws IOException {
        var requestBody = new MockDataObject();
        requestBody.setIntField(321);
        requestBody.setStringField("aaa");

        var answerBody = Utils.readTextResource("response2.json");
        mockWebServer.enqueue(new MockResponse()
                .setBody(answerBody));

        var url = apiClient.buildUrl("test");
        var data = apiClient.executeDelete(url, MockDataObject.class, requestBody);

        assertNotNull(data);
    }

    @Test
    void deleteEmpty() throws IOException {
        var requestBody = new MockDataObject();
        requestBody.setIntField(321);
        requestBody.setStringField("aaa");

        var answerBody = Utils.readTextResource("response_empty.json");
        mockWebServer.enqueue(new MockResponse()
                .setBody(answerBody));

        var url = apiClient.buildUrl("test");
        var data = apiClient.executeDelete(url, null, requestBody);

        assertNull(data);
    }

    @Test
    void constructorTest() {
        var client = new BaseOkHttpApiClient();

        assertEquals(BaseOkHttpApiClient.HEADER_API_KEY, client.getApiKeyHeader());
        assertNull(client.getHttpClient());
        assertNull(client.getConfig());
        assertNull(client.getPayloadConverter());
    }
}
