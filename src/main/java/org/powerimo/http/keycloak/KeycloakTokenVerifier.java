package org.powerimo.http.keycloak;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.powerimo.http.exceptions.TokenVerifyException;
import org.powerimo.http.okhttp.OkHttpPayloadConverter;

import java.io.IOException;
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
        // prepare Client authentication header
        String auth = keycloakParameters.getClientId() + ":" + keycloakParameters.getClientSecret();
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
        String authHeader = "Basic " + new String(encodedAuth);

        // request body
        String bodyText = "token=" + accessToken.replaceFirst("Bearer ", "");
        var body = RequestBody.create(bodyText.getBytes(StandardCharsets.UTF_8));

        Request request = new Request.Builder()
                .url(keycloakParameters.getIntrospectionUri())
                .addHeader("Authorization", authHeader)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .post(body)
                .build();

        var call = okHttpClient.newCall(request);
        var response = call.execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            String message = response.message();
            if (message.isBlank()) {
                message = "Error verifying access token. Server does not return any result";
            }
            throw new TokenVerifyException(response.code(), message);
        }
    }



}
