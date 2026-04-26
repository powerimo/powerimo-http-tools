package org.powerimo.http.keycloak;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class KeycloakUtils {

    /**
     * Builds the base URL for a Keycloak realm.
     *
     * @param serverUrl The Keycloak server URL.
     * @param realm     The realm name.
     * @return The base realm URL.
     */
    @NotNull
    @Contract(pure = true)
    public static String buildBaseRealmUrl(String serverUrl, String realm) {
        return serverUrl + "/realms/" + realm;
    }

    /**
     * Builds the token endpoint URL for a Keycloak realm.
     *
     * @param serverUrl The Keycloak server URL.
     * @param realm     The realm name.
     * @return The token endpoint URL.
     */
    @NotNull
    @Contract(pure = true)
    public static String buildTokenUrl(String serverUrl, String realm) {
        return buildBaseRealmUrl(serverUrl, realm) + "/protocol/openid-connect/token";
    }

    /**
     * Builds the logout endpoint URL for a Keycloak realm.
     *
     * @param serverUrl The Keycloak server URL.
     * @param realm     The realm name.
     * @return The logout endpoint URL.
     */
    @NotNull
    @Contract(pure = true)
    public static String buildLogoutUrl(String serverUrl, String realm) {
        return buildBaseRealmUrl(serverUrl, realm) + "/protocol/openid-connect/logout";
    }

    /**
     * Builds the userinfo endpoint URL for a Keycloak realm.
     *
     * @param serverUrl The Keycloak server URL.
     * @param realm     The realm name.
     * @return The userinfo endpoint URL.
     */
    @NotNull
    @Contract(pure = true)
    public static String buildUserInfoUrl(String serverUrl, String realm) {
        return buildBaseRealmUrl(serverUrl, realm) + "/protocol/openid-connect/userinfo";
    }

    /**
     * Builds the token introspection endpoint URL for a Keycloak realm.
     *
     * @param serverUrl The Keycloak server URL.
     * @param realm     The realm name.
     * @return The token introspection endpoint URL.
     */
    @NotNull
    @Contract(pure = true)
    public static String buildIntrospectUrl(String serverUrl, String realm) {
        return buildBaseRealmUrl(serverUrl, realm) + "/protocol/openid-connect/token/introspect";
    }

    /**
     * Builds the token revocation endpoint URL for a Keycloak realm.
     *
     * @param serverUrl The Keycloak server URL.
     * @param realm     The realm name.
     * @return The token revocation endpoint URL.
     */
    @NotNull
    @Contract(pure = true)
    public static String buildRevocationUrl(String serverUrl, String realm) {
        return buildBaseRealmUrl(serverUrl, realm) + "/protocol/openid-connect/token/revoke";
    }

}
