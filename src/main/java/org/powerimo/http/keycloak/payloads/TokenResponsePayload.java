package org.powerimo.http.keycloak.payloads;

import lombok.Data;

@Data
public class TokenResponsePayload {
    private String access_token;
    private String refresh_token;
    private String token_type;
    private String scope;
    private long expires_in;
    private long refresh_expires_in;
}
