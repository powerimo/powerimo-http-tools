package org.powerimo.http.keycloak;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powerimo.common.utils.Utils;
import org.powerimo.http.exceptions.AccessTokenObtainFailed;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class KeycloakServiceAccessTokenRequesterTest {
    private final static String CLIENT_ID = "test-backend";
    private final static String CLIENT_SECRET = "**********";
    private static MockWebServer mockWebServer;
    private static final int REQUEST_RETRIES = 3;

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    static String getMockUrl() {
        return String.format("http://localhost:%s",
                mockWebServer.getPort());
    }

    private KeycloakServiceAccessTokenRequester createClient() {
        KeycloakStaticParameters keycloakStaticParameters = KeycloakStaticParameters.builder()
                .authorizationUrl(getMockUrl())
                .clientId(CLIENT_ID)
                .clientSecret(CLIENT_SECRET)
                .build();

        KeycloakServiceAccessTokenRequester client = new KeycloakServiceAccessTokenRequester(keycloakStaticParameters);
        client.setMaxRetries(REQUEST_RETRIES);

        return client;
    }

    private void addMultipleMockResponse(MockResponse response, int amount) throws IOException {
        for (int i = 0; i < amount; i++) {
            mockWebServer.enqueue(response);
        }
    }

    @Test
    void testInvalidTokenTest() throws IOException {
        addMultipleMockResponse(new MockResponse()
                .setResponseCode(400)
                .setBody(Utils.readTextResource("keycloak/invalid_token.json")),
                REQUEST_RETRIES
        );

        var client = createClient();
        assertThrows(AccessTokenObtainFailed.class, client::requestAccessToken);
    }

    @Test
    void testRefreshToken_200() throws IOException {
        var body = Utils.readTextResource("keycloak/service_token.json");
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(body)
        );

        var client = createClient();
        client.refreshAccessToken();

        assertNotNull(client.getAccessToken());
        assertNotNull(client.getTokenResponse());
        assertNotNull(client.getTokenObtainedAt());
    }

    @Test
    void testRefreshToken_200_thirdAttempt() throws IOException {
        var bodySuccess = Utils.readTextResource("keycloak/service_token.json");
        var bodyError = Utils.readTextResource("keycloak/invalid_credentials.json");

        // unsuccessful requests
        addMultipleMockResponse(new MockResponse()
                .setResponseCode(401)
                .setBody(bodyError), 2);
        // successful request
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(bodySuccess)
        );

        var client = createClient();
        client.refreshAccessToken();

        assertNotNull(client.getAccessToken());
        assertNotNull(client.getTokenResponse());
        assertNotNull(client.getTokenObtainedAt());
    }

    @Test
    void testRefreshToken_401() throws IOException {
        addMultipleMockResponse(new MockResponse()
                .setResponseCode(401)
                .setBody(Utils.readTextResource("keycloak/invalid_credentials.json")),
                REQUEST_RETRIES
        );

        var client = createClient();
        assertThrows(AccessTokenObtainFailed.class, client::refreshAccessToken);
    }

    @Test
    void testGetAccessToken_200() throws IOException {
        var body = Utils.readTextResource("keycloak/service_token.json");
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(body)
        );

        var client = createClient();
        String s = client.getAccessToken();
        assertNotNull(s);
        assertEquals("eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJzOE5MU3hhcGhXdF9sVHlSc0V2Q0xhMk5KVXJnNXg1STR4ZHVpWGkxal9JIn0.eyJleHAiOjE3MjAxMTAyMTYsImlhdCI6MTcyMDEwOTkxNiwianRpIjoiNDc2NmM5OGItM2Y1ZC00ZTllLWJlY2ItNTBhYWI4MGVkYjYxIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDoxMDAwL3JlYWxtcy9kZXYiLCJzdWIiOiI4ZGZkZDY1Ni1iNzkwLTQzODMtYWI2MS0zODUwYjgyMmUxYWIiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJ0ZXN0LWJhY2tlbmQiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbIi8qIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJkZWZhdWx0LXJvbGVzLWRldiIsIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iXX0sInNjb3BlIjoiZW1haWwgcHJvZmlsZSIsImNsaWVudEhvc3QiOiIxNzIuMTkuMC4xIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJzZXJ2aWNlLWFjY291bnQtdGVzdC1iYWNrZW5kIiwiY2xpZW50QWRkcmVzcyI6IjE3Mi4xOS4wLjEiLCJjbGllbnRfaWQiOiJ0ZXN0LWJhY2tlbmQifQ.Ub6POb3MtqGUOFvs-4aoHpQcPFRtOeMMzwK_F_f2xwfs9H9s1eRBNcWor0_FJ9SaRWT_3jOiX80xbW9lc47qmyqyLmD0t9MWdoRjPbvUu1ZhzJQe4oT2S8bVX0dkWKCDnx7kX-tv0AMEb0_Lbw7sGOi-xuj02kCTKsVR0am1W5PYb7KEJNidO1wNff732bZvVhg8zpvvmPAHWj5HmY5c2few-DT9tBb83NX6FX6vKWfM9QUyVDHDyC0ECfM1JqblDiVv7lSY8EjUW58EE2474eZJ9gCv653hoGqMK0Ot7J-xK9ZnS7RuKDp1FbayK8BysUPKQ1uBAz-AKI58UVQMYg", s);
    }

    @Test
    void testIncompleteClientSettings() {
        var client = createClient();
        KeycloakStaticParameters staticParameters = (KeycloakStaticParameters) client.getKeycloakParameters();

        staticParameters.setClientId(null);
        assertThrows(AccessTokenObtainFailed.class, client::refreshAccessToken);

        staticParameters.setClientId(CLIENT_ID);
        staticParameters.setClientSecret(null);
        assertThrows(AccessTokenObtainFailed.class, client::refreshAccessToken);
    }

    @Test
    void testPropertiesAreCorrect() {
        var client = createClient();

        assertEquals(CLIENT_ID, client.getKeycloakParameters().getClientId());
        assertEquals(CLIENT_SECRET, client.getKeycloakParameters().getClientSecret());
    }
}
