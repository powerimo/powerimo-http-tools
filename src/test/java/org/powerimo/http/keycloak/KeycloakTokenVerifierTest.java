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
    public void devTest() throws IOException {
        String s = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJzOE5MU3hhcGhXdF9sVHlSc0V2Q0xhMk5KVXJnNXg1STR4ZHVpWGkxal9JIn0.eyJleHAiOjE3MjA0NDA3ODAsImlhdCI6MTcyMDQ0MDQ4MCwiYXV0aF90aW1lIjoxNzIwNDM4NDcwLCJqdGkiOiI2ZGMwOTE1Mi05MGI4LTQxOGUtYmFmYi1lMjU4NDEzNjkyMGQiLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjEwMDAvcmVhbG1zL2RldiIsInN1YiI6ImRjZDhlOGMyLTFiYzUtNDc4OC1iNmRjLThjYzViMjZhYjAzZSIsInR5cCI6IkJlYXJlciIsImF6cCI6InRlc3QtZnJvbnRlbmQiLCJzZXNzaW9uX3N0YXRlIjoiMzdkNGIzNTktZjhlYi00YTA2LTg2NGQtNjIzNzI3ZTUzNjdiIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyIiLCJodHRwOi8vbG9jYWxob3N0Il0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJkZWZhdWx0LXJvbGVzLWRldiIsIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iXX0sInNjb3BlIjoiZW1haWwgcHJvZmlsZSIsInNpZCI6IjM3ZDRiMzU5LWY4ZWItNGEwNi04NjRkLTYyMzcyN2U1MzY3YiIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwibmFtZSI6InVzZXIwMkBlbWFpbC5vcmcgdXNlcjAyQGVtYWlsLm9yZyIsInByZWZlcnJlZF91c2VybmFtZSI6InVzZXIwMkBlbWFpbC5vcmciLCJnaXZlbl9uYW1lIjoidXNlcjAyQGVtYWlsLm9yZyIsImZhbWlseV9uYW1lIjoidXNlcjAyQGVtYWlsLm9yZyIsImVtYWlsIjoidXNlcjAyQGVtYWlsLm9yZyJ9.tzo31AcNUk07EBxsU51hDZftv3lxT0yz67ZSLIn4Jp90v0zlPJh6kFAQFl2D7XG4yN00PHn99GwT6tBDEFeeriqZihWBjm7KFiA3wcXLO5gqXZm_yJNdrN0N-wSXvHwlD_xZ_UH8AiE28me78PhCr1ZQRYos2H92MnEUswjC65HPBhU7VKi77h-dbe_IWfoqDGZwwoESubnmxfQO5BAWJQ-xRyzCThx7mBTesKIIPxWM7vtmx3X9WgKGzne2qwA3xiS-G5lQm3_LLIAn8f2rvJF0t2_PQHtKr4Sou944xpIff1rKfAKm79bnUs8NBYi9D5Ks09csqgs9aPUyN0BjZg";
        KeycloakStaticParameters parameters = (KeycloakStaticParameters) createParameters();
        parameters.setClientSecret("**********");
        var verifier = new KeycloakTokenVerifier(parameters);
        var result = verifier.introspect(s);

        assertNotNull(result);
        assertTrue(result.contains("\"active\":true"));
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
        assertEquals(1719600731, tokenInfo.getAuthTime());
        assertEquals("8b27d9d8-74d0-4349-8dfe-7a121f09c3d4", tokenInfo.getJti());
        assertEquals("http://localhost:1000/realms/dev", tokenInfo.getIss());
        assertEquals(1, tokenInfo.getAud().size());
        assertEquals("894e830c-5ab3-406a-b109-2236b9fc35dd", tokenInfo.getSub());
        assertEquals("Bearer", tokenInfo.getTyp());
        assertEquals("account-console", tokenInfo.getAzp());
        assertEquals("4d0959e2-88ae-42ad-baa5-7eb31e7889fa", tokenInfo.getNonce());
        assertEquals("267d186b-86a2-4e74-98c0-ae55a6e591cc", tokenInfo.getSessionState());
        assertEquals("1", tokenInfo.getAcr());
        assertEquals("openid email profile", tokenInfo.getScope());
        assertEquals("267d186b-86a2-4e74-98c0-ae55a6e591cc", tokenInfo.getSid());
        assertFalse(tokenInfo.isEmailVerified());
        assertEquals("User 01", tokenInfo.getName());
        assertEquals("user01@email.org", tokenInfo.getPreferredUsername());
        assertEquals("User", tokenInfo.getGivenName());
        assertEquals("01", tokenInfo.getFamilyName());
        assertEquals("user01@email.org", tokenInfo.getEmail());
        assertEquals("account-console", tokenInfo.getClientId());
        assertEquals("user01@email.org", tokenInfo.getUsername());
        assertEquals("Bearer", tokenInfo.getTokenType());
        assertTrue(tokenInfo.isActive());
    }

    @Test
    void testJsonIntrospect2_success() throws IOException {
        var json = Utils.readTextResource("keycloak/introspect_success_2.json");
        var converter = new DefaultPayloadConverter();

        IntrospectResponsePayload tokenInfo = converter.deserialize(json, IntrospectResponsePayload.class);

        assertNotNull(tokenInfo);
        assertEquals(1720514903, tokenInfo.getExp());
        assertEquals(1720514303, tokenInfo.getIat());
        assertEquals(1720512686, tokenInfo.getAuthTime());
        assertEquals("7cd4959c-d574-4718-ae1b-36fb79d55926", tokenInfo.getJti());
        assertEquals("http://localhost:1000/realms/dev", tokenInfo.getIss());
        assertEquals("dcd8e8c2-1bc5-4788-b6dc-8cc5b26ab03e", tokenInfo.getSub());
        assertEquals("Bearer", tokenInfo.getTyp());
        assertEquals("test-frontend", tokenInfo.getAzp());
        assertEquals("15e45720-7fe2-4e81-a27f-116c42103dbd", tokenInfo.getSessionState());
        assertEquals("0", tokenInfo.getAcr());
        assertEquals("email profile", tokenInfo.getScope());
        assertEquals("15e45720-7fe2-4e81-a27f-116c42103dbd", tokenInfo.getSid());
        assertFalse(tokenInfo.isEmailVerified());
        assertEquals("user02", tokenInfo.getName());
        assertEquals("user02@email.org", tokenInfo.getPreferredUsername());
        assertEquals("User", tokenInfo.getGivenName());
        assertEquals("02", tokenInfo.getFamilyName());
        assertEquals("user02@email.org", tokenInfo.getEmail());
        assertEquals("test-frontend", tokenInfo.getClientId());
        assertEquals("user02@email.org", tokenInfo.getUsername());
        assertEquals("Bearer", tokenInfo.getTokenType());
        assertTrue(tokenInfo.isActive());
        assertNotNull(tokenInfo.getRealmAccess());
        assertNotNull(tokenInfo.getRealmAccess().getRoles());
        assertEquals(3, tokenInfo.getRealmAccess().getRoles().size());
    }
}
