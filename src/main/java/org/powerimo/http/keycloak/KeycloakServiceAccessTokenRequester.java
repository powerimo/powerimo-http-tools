package org.powerimo.http.keycloak;

import lombok.Getter;
import lombok.Setter;
import okhttp3.*;
import org.powerimo.http.exceptions.AccessTokenObtainFailed;
import org.powerimo.http.exceptions.ApiClientException;
import org.powerimo.http.keycloak.payloads.TokenResponsePayload;
import org.powerimo.http.okhttp.DefaultPayloadConverter;
import org.powerimo.http.okhttp.OkHttpPayloadConverter;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.logging.Logger;

@Getter
@Setter
public class KeycloakServiceAccessTokenRequester {
    private final Logger logger = Logger.getLogger(KeycloakServiceAccessTokenRequester.class.getName());
    private String authUrl;
    private String clientId;
    private String clientSecret;
    private String scope;
    private String grantType = "client_credentials";
    private String accessToken;
    private final OkHttpClient httpClient;
    private OkHttpPayloadConverter payloadConverter = new DefaultPayloadConverter();
    private TokenResponsePayload tokenResponse;
    private Instant tokenObtainedAt;
    private int maxRetries = 3;
    private int currentRetries = 0;

    public KeycloakServiceAccessTokenRequester() {
        httpClient = new OkHttpClient();
    }

    public KeycloakServiceAccessTokenRequester(String authUrl, String clientId, String clientSecret) {
        super();
        this.authUrl = authUrl;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        httpClient = new OkHttpClient();
    }

    public void refreshAccessToken() {
        var tokenPayload = requestAccessToken();
        var newTokenResponsePayload = payloadConverter.deserialize(tokenPayload, TokenResponsePayload.class);
        if (newTokenResponsePayload.getAccess_token() == null) {
            throw new AccessTokenObtainFailed("Authentication server returns 2xx status, but token is missing in payload", newTokenResponsePayload.toString());
        }
        tokenResponse = newTokenResponsePayload;
        tokenObtainedAt = Instant.now();
    }

    public String requestAccessToken() {
        if (clientId == null)
            throw new AccessTokenObtainFailed("clientId is required", "incomplete credentials");
        if (clientSecret == null) {
            throw new AccessTokenObtainFailed("clientSecret is required", "incomplete credentials");
        }

        int attempt = 0;
        IOException lastException = null;
        StringBuilder stringBuilder = new StringBuilder();

        while (attempt < maxRetries) {
            attempt++;
            try (var response = tryToGetAccessToken()) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        return response.body().string();
                    } else {
                        return null;
                    }
                } else {
                    String message = response.code() + " : " + response.message();
                    if (response.body() != null) {
                        message = response.code() + " : " + response.body().string();
                    }
                    stringBuilder.append(attempt).append(": ").append(message).append("\n");
                    logger.info("Attempt " + attempt + " of " + maxRetries + " failed: " + message);
                }
            } catch (IOException e) {
                lastException = e;
                stringBuilder.append(attempt).append(": ").append(e.getMessage()).append("\n");
                logger.info("Attempt " + attempt + " failed: " + e.getMessage());
            }
        }

        if (lastException != null) {
            throw new AccessTokenObtainFailed("Failed to obtain access token after " + maxRetries + " attempts", stringBuilder.toString(), lastException);
        } else {
            throw new AccessTokenObtainFailed("Failed to obtain access token after " + maxRetries + " attempts", stringBuilder.toString());
        }
    }

    protected Response tryToGetAccessToken() throws IOException {
        RequestBody body = new FormBody.Builder()
                .add("client_id", clientId)
                .add("client_secret", clientSecret)
                .add("grant_type", grantType)
                .build();

        Request request = new Request.Builder()
                .url(authUrl)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .post(body)
                .build();

        var call = getHttpClient().newCall(request);
        return call.execute();
    }

    public String getAccessToken() {
        boolean needsRefresh = tokenObtainedAt == null || tokenResponse == null;

        if (!needsRefresh) {
            var expiresAt = tokenObtainedAt.plus(tokenResponse.getExpires_in(), ChronoUnit.SECONDS);
            needsRefresh = (expiresAt.isBefore(Instant.now()));
        }

        if (needsRefresh) {
            refreshAccessToken();
        }

        return tokenResponse == null ? null : tokenResponse.getAccess_token();
    }
}
