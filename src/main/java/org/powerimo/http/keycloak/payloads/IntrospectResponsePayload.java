package org.powerimo.http.keycloak.payloads;

import lombok.Data;

@Data
public class IntrospectResponsePayload {
    private long exp;
    private long iat;
    private long auth_time;
    private String jti;
    private String iss;
    private String aud;
    private String sub;
    private String typ;
    private String azp;
    private String nonce;
    private String session_state;
    private String acr;
    private String scope;
    private String sid;
    private boolean email_verified;
    private String name;
    private String preferred_username;
    private String given_name;
    private String family_name;
    private String email;
    private String client_id;
    private String username;
    private String token_type;
    private boolean active;
}
