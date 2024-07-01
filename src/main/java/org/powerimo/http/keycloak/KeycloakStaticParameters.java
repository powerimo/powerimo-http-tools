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
    private String url;
    private String clientId;
    private String clientSecret;
    private String realm;

    @Override
    public String getIntrospectionUri() {
        return url + "/realms/" + realm + "/protocol/openid-connect/token/introspect";
    }
}
