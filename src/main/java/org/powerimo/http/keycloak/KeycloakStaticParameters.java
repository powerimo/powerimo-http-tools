package org.powerimo.http.keycloak;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeycloakStaticParameters implements KeycloakParameters {
    private String serverUrl;
    private String clientId;
    private String clientSecret;
    private String realm;

    @Override
    public String getIntrospectionUrl() {
        return serverUrl + "/realms/" + realm + "/protocol/openid-connect/token/introspect";
    }

    @Override
    public String getAuthorizationUrl() {
        return serverUrl + "/realms/" + realm + "/protocol/openid-connect/token";
    }
}
