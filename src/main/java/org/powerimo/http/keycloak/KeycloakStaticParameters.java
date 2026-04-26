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
    private String tokenUrl;
    private String introspectionUrl;

    @Override
    public String getIntrospectionUrl() {
        return introspectionUrl == null ? KeycloakUtils.buildIntrospectUrl(serverUrl, realm) : introspectionUrl;
    }

    @Override
    public String getTokenUrl() {
        return tokenUrl == null ? KeycloakUtils.buildTokenUrl(serverUrl, realm) : tokenUrl;
    }
}
