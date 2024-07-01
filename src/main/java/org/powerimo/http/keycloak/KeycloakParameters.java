package org.powerimo.http.keycloak;

public interface KeycloakParameters {
    String getUrl();
    String getRealm();
    String getClientId();
    String getClientSecret();
    String getIntrospectionUri();
}
