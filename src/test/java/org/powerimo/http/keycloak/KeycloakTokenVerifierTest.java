package org.powerimo.http.keycloak;

import org.junit.jupiter.api.Test;
import org.powerimo.common.utils.Utils;
import org.powerimo.http.keycloak.payloads.IntrospectResponsePayload;
import org.powerimo.http.okhttp.DefaultPayloadConverter;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class KeycloakTokenVerifierTest {
    private static KeycloakParameters createParameters() {
        return KeycloakStaticParameters.builder()
                .serverUrl("http://localhost:1000")
                .clientId("test-backend")
                .clientSecret("TrWbdfeCFrkN0LhQj093F3RDtOW9q9eC")
                .realm("dev")
                .build();
    }

    //@Test
    public void test_nonExisting() throws IOException {
        var verifier = new KeycloakTokenVerifier(createParameters());
        String responseText = verifier.introspect("Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJEVm1SRmt2cmJPZTVoLUpEeHhTWWFDU1ZQRGFWcGhZMVNOMXNvNmRuMXJVIn0.eyJleHAiOjE3MTk2MDEwMzMsImlhdCI6MTcxOTYwMDczMywiYXV0aF90aW1lIjoxNzE5NjAwNzMxLCJqdGkiOiI4YjI3ZDlkOC03NGQwLTQzNDktOGRmZS03YTEyMWYwOWMzZDQiLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjEwMDAvcmVhbG1zL2RldiIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiI4OTRlODMwYy01YWIzLTQwNmEtYjEwOS0yMjM2YjlmYzM1ZGQiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJhY2NvdW50LWNvbnNvbGUiLCJub25jZSI6IjRkMDk1OWUyLTg4YWUtNDJhZC1iYWE1LTdlYjMxZTc4ODlmYSIsInNlc3Npb25fc3RhdGUiOiIyNjdkMTg2Yi04NmEyLTRlNzQtOThjMC1hZTU1YTZlNTkxY2MiLCJhY3IiOiIxIiwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyJdfX0sInNjb3BlIjoib3BlbmlkIGVtYWlsIHByb2ZpbGUiLCJzaWQiOiIyNjdkMTg2Yi04NmEyLTRlNzQtOThjMC1hZTU1YTZlNTkxY2MiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsIm5hbWUiOiJVc2VyIDAxIiwicHJlZmVycmVkX3VzZXJuYW1lIjoidXNlcjAxQGVtYWlsLm9yZyIsImdpdmVuX25hbWUiOiJVc2VyIiwiZmFtaWx5X25hbWUiOiIwMSIsImVtYWlsIjoidXNlcjAxQGVtYWlsLm9yZyJ9.d9KMDpVdbdchpWch2hhYAAYn3TOsV85RliSlLkVknLNlxCpzAaQkMPQOT3ze7sW7908P4lL9VpTzkFl_QWafB7yM1UJgAyJ-ioWdrs0AZaX6w0YV3cMhHAg7TcudM5IGfurmTpFe-YoQ4pdvrWvPpwmGdu7RJ8LnHh83l5e9q5zIgwSckc4j2v6020T5MKWvhmsJTikiQdnKA3B16dPu2xEiboYO1RKqQYHsYBwSlcuQoDdaGeSmdRHy2LiY7orrpNyV_8ILzvHMIRQWoPWVymYTcT62YTE02bXnRD7y2Sj9uXiIUiUjXOS7CxJZAzf-F4GO-PrZKKD3q6ft7yLklA");

        assertNotNull(responseText);
    }

    @Test
    void jsonIntrospect_success() throws IOException {
        var json = Utils.readTextResource("keycloak/introspect_success.json");
        var converter = new DefaultPayloadConverter();

        IntrospectResponsePayload tokenInfo = converter.deserialize(json, IntrospectResponsePayload.class);

        assertNotNull(tokenInfo);
        assertEquals(1719601033, tokenInfo.getExp());
        assertEquals(1719600733, tokenInfo.getIat());
        assertEquals(1719600731, tokenInfo.getAuth_time());
        assertEquals("8b27d9d8-74d0-4349-8dfe-7a121f09c3d4", tokenInfo.getJti());
        assertEquals("http://localhost:1000/realms/dev", tokenInfo.getIss());
        assertEquals("account", tokenInfo.getAud());
        assertEquals("894e830c-5ab3-406a-b109-2236b9fc35dd", tokenInfo.getSub());
        assertEquals("Bearer", tokenInfo.getTyp());
        assertEquals("account-console", tokenInfo.getAzp());
        assertEquals("4d0959e2-88ae-42ad-baa5-7eb31e7889fa", tokenInfo.getNonce());
        assertEquals("267d186b-86a2-4e74-98c0-ae55a6e591cc", tokenInfo.getSession_state());
        assertEquals("1", tokenInfo.getAcr());
        assertEquals("openid email profile", tokenInfo.getScope());
        assertEquals("267d186b-86a2-4e74-98c0-ae55a6e591cc", tokenInfo.getSid());
        assertFalse(tokenInfo.isEmail_verified());
        assertEquals("User 01", tokenInfo.getName());
        assertEquals("user01@email.org", tokenInfo.getPreferred_username());
        assertEquals("User", tokenInfo.getGiven_name());
        assertEquals("01", tokenInfo.getFamily_name());
        assertEquals("user01@email.org", tokenInfo.getEmail());
        assertEquals("account-console", tokenInfo.getClient_id());
        assertEquals("user01@email.org", tokenInfo.getUsername());
        assertEquals("Bearer", tokenInfo.getToken_type());
        assertTrue(tokenInfo.isActive());
    }
}
