package org.powerimo.http.keycloak;

public interface KeycloakParameters {
    String getServerUrl();
    String getRealm();
    String getClientId();
    String getClientSecret();
    String getIntrospectionUrl();
    String getAuthorizationUrl();
}
