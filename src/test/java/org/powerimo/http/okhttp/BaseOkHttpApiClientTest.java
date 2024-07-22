package org.powerimo.http.okhttp;

import okhttp3.Request;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.powerimo.common.utils.Utils;
import org.powerimo.http.MockDataObject;
import org.powerimo.http.exceptions.ApiCallException;
import org.powerimo.http.exceptions.PayloadConvertException;

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
        apiClient.setPayloadConverter(new EnvelopePayloadConverter());

        mockWebServer = new MockWebServer();
        config.setUrl("http://" + mockWebServer.getHostName() + ":" + mockWebServer.getPort());
    }

    @Test
    void buildUrl_1() {
        var url = apiClient.buildUrl("v1/account");

        assertEquals(config.getUrl() + "/v1/account", url);
    }

    @Test
    void buildUrl_2() {
        var url = apiClient.buildUrl("v1/account", "aaa", "bbb");

        assertEquals(config.getUrl() + "/v1/account/aaa/bbb", url);
    }

    @Test
    void buildUrl_3() {
        var url = apiClient.buildUrl("v1/account", 3, 12);

        assertEquals(config.getUrl() + "/v1/account/3/12", url);
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
    void delete_withBody() throws IOException {
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
    void delete_withEmptyBody() throws IOException {
        var answerBody = Utils.readTextResource("response_empty.json");
        mockWebServer.enqueue(new MockResponse()
                .setBody(answerBody));

        var url = apiClient.buildUrl("test");
        var data = apiClient.executeDelete(url, null, null);

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

    @Test
    void execute_400_nonEnveloped() throws IOException {
        var body = Utils.readTextResource("400.json");
        mockWebServer.enqueue(new MockResponse()
                .setBody(body)
                .setResponseCode(400));
        var url = apiClient.buildUrl("test");
        var ex = assertThrows(ApiCallException.class, () -> apiClient.executeGet(url, MockDataObject.class));

        assertNotNull(ex);
        assertEquals(body, ex.getResponseBody());
    }

    @Test
    void execute_400_enveloped() throws IOException {
        var body = Utils.readTextResource("400enveloped.json");
        mockWebServer.enqueue(new MockResponse()
                .setBody(body)
                .setResponseCode(400));
        var url = apiClient.buildUrl("test");
        var ex = assertThrows(ApiCallException.class, () -> apiClient.executeGet(url, MockDataObject.class));

        assertNotNull(ex);
        assertEquals(400, ex.getHttpCode());
        assertEquals("ERR-00400", ex.getApiMessageCode());
        assertEquals("Bad request (400). This is the sample message.", ex.getMessage());
        assertEquals(body, ex.getResponseBody());
    }

    @Test
    void execute_400_emptyBody() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(400));
        var url = apiClient.buildUrl("test");
        var ex = assertThrows(ApiCallException.class, () -> apiClient.executeGet(url, MockDataObject.class));

        assertNotNull(ex);
        assertEquals(400, ex.getHttpCode());
    }

    @Test
    void executePut_success() throws IOException {
        var url = apiClient.buildUrl("test");
        var body = Utils.readTextResource("response2.json");
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(body));

        var data = apiClient.executePut(url, MockDataObject.class, "SAMPLE REQUEST DATA");

        assertNotNull(data);
    }

    @Test
    void execute_400_brokenJson() {
        mockWebServer.enqueue(new MockResponse()
                .setBody("{\"someAttribute\": \"123\", }")
                .setResponseCode(400));
        var url = apiClient.buildUrl("test");
        var ex = assertThrows(ApiCallException.class, () -> apiClient.executeGet(url, MockDataObject.class));

        assertNotNull(ex);
        assertEquals(400, ex.getHttpCode());
        assertNotNull(ex.getCause());
        assertInstanceOf(PayloadConvertException.class, ex.getCause());
    }

    @Test
    void payloadConverter_autoCreate() {
        BaseOkHttpApiClient client = new BaseOkHttpApiClient();

        assertNull(client.getPayloadConverter());

        client.checkPayloadConverter();

        assertNotNull(client.getPayloadConverter());
        assertInstanceOf(DefaultPayloadConverter.class, client.getPayloadConverter());
    }
}
