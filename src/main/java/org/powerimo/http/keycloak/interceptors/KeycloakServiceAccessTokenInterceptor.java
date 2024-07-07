package org.powerimo.http.keycloak.interceptors;

import okhttp3.Interceptor;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.powerimo.http.keycloak.KeycloakServiceAccessTokenRequester;

import java.io.IOException;

public class KeycloakServiceAccessTokenInterceptor implements Interceptor {
    private final KeycloakServiceAccessTokenRequester requester;

    public KeycloakServiceAccessTokenInterceptor(KeycloakServiceAccessTokenRequester requester) {
        this.requester = requester;
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        var request = chain.request();
        var authorizedRequest = request.newBuilder()
                .addHeader("Authorization", requester.getAccessToken())
                .build();
        return chain.proceed(authorizedRequest);
    }


}
