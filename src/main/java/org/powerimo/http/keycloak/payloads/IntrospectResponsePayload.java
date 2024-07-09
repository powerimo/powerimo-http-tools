package org.powerimo.http.keycloak.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class IntrospectResponsePayload {
    private long exp;
    private long iat;

    @JsonProperty("auth_time")
    private long authTime;
    private String jti;
    private String iss;
    private String aud;
    private String sub;
    private String typ;
    private String azp;
    private String nonce;

    @JsonProperty("session_state")
    private String sessionState;
    private String acr;
    private String scope;
    private String sid;

    @JsonProperty("email_verified")
    private boolean emailVerified;
    private String name;

    @JsonProperty("preferred_username")
    private String preferredUsername;

    @JsonProperty("given_name")
    private String givenName;

    @JsonProperty("family_name")
    private String familyName;
    private String email;

    @JsonProperty("client_id")
    private String clientId;
    private String username;

    @JsonProperty("token_type")
    private String tokenType;
    private boolean active;

    @JsonProperty("allowed-origins")
    private List<String> allowedOrigins;

    @JsonProperty("realm_access")
    private RealmAccess realmAccess = new RealmAccess();

    @Data
    public static class RealmAccess {
        private List<String> roles = new ArrayList<>();
    }
}
