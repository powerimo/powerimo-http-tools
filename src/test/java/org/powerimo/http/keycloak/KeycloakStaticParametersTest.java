package org.powerimo.http.keycloak;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KeycloakStaticParametersTest {
    private final static String SERVER_URL = "http://localhost:1000";
    private final static String REALM_NAME = "test";
    private final static String CLIENT_ID = "test-client";
    private final static String CLIENT_SECRET = "test-client-secret";

    private KeycloakStaticParameters createTestParameters() {
        return KeycloakStaticParameters.builder()
                .serverUrl(SERVER_URL)
                .realm(REALM_NAME)
                .clientId(CLIENT_ID)
                .clientSecret(CLIENT_SECRET)
                .build();
    }

    @Test
    void testGetIntrospectionUrl_default() {
        var p = createTestParameters();
        assertEquals(SERVER_URL + "/realms/" + REALM_NAME + "/protocol/openid-connect/token/introspect", p.getIntrospectionUrl());
        assertEquals(CLIENT_ID, p.getClientId());
        assertEquals(CLIENT_SECRET, p.getClientSecret());
        assertEquals(REALM_NAME, p.getRealm());
        assertEquals(SERVER_URL, p.getServerUrl());
    }

    @Test
    void testGetIntrospectionUrl_custom() {
        var p = createTestParameters();
        p.setIntrospectionUrl("http://localhost:1001");
        assertEquals("http://localhost:1001", p.getIntrospectionUrl());
    }

    @Test
    void testGetAuthorizationUrl_default() {
        var p = createTestParameters();
        assertEquals(SERVER_URL + "/realms/" + REALM_NAME + "/protocol/openid-connect/token", p.getAuthorizationUrl());
    }

    @Test
    void testGetAuthorizationUrl_custom() {
        var p = createTestParameters();
        p.setAuthorizationUrl("http://localhost:1001");
        assertEquals("http://localhost:1001", p.getAuthorizationUrl());
    }
}