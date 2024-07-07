package org.powerimo.http.keycloak.interceptors;

import okhttp3.Interceptor;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.powerimo.http.exceptions.ApiClientException;
import org.powerimo.http.keycloak.KeycloakParameters;
import org.powerimo.http.keycloak.KeycloakServiceAccessTokenRequester;

import java.io.IOException;

public class KeycloakServiceAccessTokenInterceptor implements Interceptor {
    private final KeycloakServiceAccessTokenRequester requester;

    public KeycloakServiceAccessTokenInterceptor(KeycloakServiceAccessTokenRequester requester) {
        this.requester = requester;
    }

    public KeycloakServiceAccessTokenInterceptor(KeycloakParameters keycloakParameters) {
        this.requester = new KeycloakServiceAccessTokenRequester(keycloakParameters);
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        if (requester == null)
            throw new ApiClientException("Requester not set");

        var request = chain.request();
        var authorizedRequest = request.newBuilder()
                .addHeader("Authorization", requester.getAccessToken())
                .build();
        return chain.proceed(authorizedRequest);
    }


}
