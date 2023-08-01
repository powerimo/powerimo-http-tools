package org.powerimo.http.okhttp;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.powerimo.http.exceptions.ApiCallException;
import org.powerimo.http.exceptions.ApiClientException;

import java.io.IOException;

@Getter
@Setter
public class BaseOkHttpApiClient {
    private OkHttpClient httpClient;
    private BaseOkHttpClientConfig config;
    private OkHttpPayloadConverter payloadConverter;
    public static final String HEADER_API_KEY = "x-api-key";

    protected void checkConfig() {
        if (config == null)
            throw new ApiClientException("Config is not specified.");
    }

    protected void checkHttpClient() {
        if (httpClient == null) {
            httpClient = new OkHttpClient();
        }
    }

    protected void checkPayloadConverter() {
        if (payloadConverter == null)
            throw new ApiClientException("PayloadConverter is not specified.");
    }

    public Response executeRequest(@NonNull Request request) throws IOException {
        checkHttpClient();
        checkConfig();

        Request prepared = request.newBuilder()
                .addHeader(HEADER_API_KEY, config.getApiKey())
                .build();

        var call = httpClient.newCall(prepared);
        var response = call.execute();
        if (response.isSuccessful())
            return response;
        if (response.body() != null) {
            throw new ApiCallException(response.code(), response.body().string());
        } else {
            throw new ApiCallException(response.code(), response.message());
        }
    }

    public <T> T executeRequest(@NonNull Request request, @NonNull Class<T> payloadClass, boolean allowEmptyBody) {
        try {
            var response = executeRequest(request);
            return extractBody(payloadClass, response, allowEmptyBody);
        } catch (IOException ex) {
            throw new ApiClientException("Exception on executing the request", ex);
        }
    }

    public <T> T extractBody(@NonNull Class<T> payloadClass, @NonNull Response response, boolean allowEmptyBody) throws IOException {
        checkPayloadConverter();

        if (response.body() != null) {
            var envelope = payloadConverter.convertEnvelope(response.body().string(), payloadClass);
            return envelope.getData();
        }

        if (allowEmptyBody) {
            return null;
        }
        throw new ApiClientException("Response body is empty");
    }

    public <T> T executeGet(@NonNull String url, Class<T> payloadClass) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        return executeRequest(request, payloadClass, false);
    }

    public <T> T executePost(@NonNull String url, Class<T> payloadClass, Object body) {
        var requestBody = payloadConverter.serialize(body);

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        return executeRequest(request, payloadClass, true);
    }

    public String buildUrl(Object... args) {
        checkConfig();
        StringBuilder url = new StringBuilder(config.getUrl());
        for (var item: args) {
            url.append("/").append(item);
        }
        return url.toString();
    }

}
