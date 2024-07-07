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
    private String authorizationUrl;
    private String introspectionUrl;

    @Override
    public String getIntrospectionUrl() {
        return introspectionUrl == null ? serverUrl + "/realms/" + realm + "/protocol/openid-connect/token/introspect" : introspectionUrl;
    }

    @Override
    public String getAuthorizationUrl() {
        return authorizationUrl == null ? serverUrl + "/realms/" + realm + "/protocol/openid-connect/token" : authorizationUrl;
    }
}
