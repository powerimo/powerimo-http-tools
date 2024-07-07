package org.powerimo.http.keycloak;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import okhttp3.*;
import org.powerimo.http.exceptions.TokenVerifyException;
import org.powerimo.http.okhttp.OkHttpPayloadConverter;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
public class KeycloakTokenVerifier {
    private final KeycloakParameters keycloakParameters;
    private OkHttpClient okHttpClient;
    private OkHttpPayloadConverter payloadConverter;

    public KeycloakTokenVerifier(KeycloakParameters keycloakParameters) {
        this.keycloakParameters = keycloakParameters;
        initHttpClient();
    }

    protected void initHttpClient() {
        if (okHttpClient != null)
            return;

        okHttpClient = new OkHttpClient.Builder()
                .callTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
                .build();
    }

    public String introspect(@NonNull String accessToken) throws IOException {
        // Prepare client authentication header
        String clientId = keycloakParameters.getClientId();
        String clientSecret = keycloakParameters.getClientSecret();
        String auth = clientId + ":" + clientSecret;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        String authHeader = "Basic " + encodedAuth;

        // Clean token
        String cleanToken = accessToken.replaceFirst("Bearer ", "");

        // Request body
        RequestBody body = new FormBody.Builder()
                .add("token", cleanToken)
                .build();

        // Build the request
        Request request = new Request.Builder()
                .url(keycloakParameters.getIntrospectionUrl())
                .addHeader("Authorization", authHeader)
                .post(body)
                .build();

        // Execute the request and handle the response
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                ResponseBody responseBody = response.body();
                if (responseBody != null) {
                    return responseBody.string();
                } else {
                    throw new TokenVerifyException(response.code(), "Empty response body");
                }
            } else {
                String message = response.message();
                if (message.isBlank()) {
                    message = "Error verifying access token. Server did not return any result.";
                }
                throw new TokenVerifyException(response.code(), message);
            }
        }
    }


}
