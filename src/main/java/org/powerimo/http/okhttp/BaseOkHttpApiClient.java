package org.powerimo.http.okhttp;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.powerimo.http.Envelope;
import org.powerimo.http.exceptions.ApiCallException;
import org.powerimo.http.exceptions.ApiClientException;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
public class BaseOkHttpApiClient implements Serializable {
    @Serial
    private final static long serialVersionUID = -8595931108573302900L;

    private OkHttpClient httpClient;
    private BaseOkHttpClientConfig config;
    private OkHttpPayloadConverter payloadConverter;
    private String apiKeyHeader = HEADER_API_KEY;
    private OkHttpPayloadConverter errorEnvelopeConverter = new DefaultPayloadConverter();

    public static final String HEADER_API_KEY = "x-api-key";

    protected void checkConfig() {
        if (config == null)
            throw new ApiClientException("Config is not specified.");
    }

    protected void checkHttpClient() {
        checkConfig();
        if (httpClient == null) {
            httpClient = new OkHttpClient.Builder()
                    .callTimeout(config.getCallTimeout(), TimeUnit.SECONDS)
                    .readTimeout(config.getCallTimeout(), TimeUnit.SECONDS)
                    .connectTimeout(config.getConnectTimeout(), TimeUnit.SECONDS)
                    .build();
        }
    }

    protected void checkPayloadConverter() {
        if (payloadConverter == null) {
            payloadConverter = new DefaultPayloadConverter();
        }
    }

    public Response executeRequest(@NonNull Request request) throws IOException {
        checkHttpClient();
        checkConfig();

        Request prepared;
        if (config.getUseApiKey()) {
            prepared = request.newBuilder()
                    .addHeader(apiKeyHeader, config.getApiKey())
                    .build();
        } else {
            prepared = request;
        }

        var call = httpClient.newCall(prepared);
        var response = call.execute();
        if (response.isSuccessful()) {
            return response;
        } else {
            return handleExecuteRequestUnsuccessful(request, response);
        }
    }

    public Response handleExecuteRequestUnsuccessful(@NonNull Request request, @NonNull Response response) throws IOException {
        if (response.body() != null) {
            String body = response.body().string();

            Envelope<?> envelope;
            try {
                envelope = errorEnvelopeConverter.deserialize(body, Envelope.class);
            } catch (Exception ex) {
                throw new ApiCallException(response.code(), body, ex);
            }

            throw new ApiCallException(response.code(), envelope.getMessageCode(), envelope.getMessage(), body);
        } else {
            throw new ApiCallException(response.code(), response.message());
        }
    }

    public <T> T executeRequest(@NonNull Request request, Class<T> payloadClass, boolean allowEmptyBody) {
        try {
            var response = executeRequest(request);
            if (payloadClass != null) {
                return extractBody(payloadClass, response, allowEmptyBody);
            } else {
                return null;
            }
        } catch (IOException ex) {
            throw new ApiClientException("Exception on executing the request", ex);
        }
    }

    public <T> T extractBody(@NonNull Class<T> payloadClass, @NonNull Response response, boolean allowEmptyBody) throws IOException {
        checkPayloadConverter();

        if (response.body() != null) {
            return payloadConverter.deserialize(response.body().string(), payloadClass);
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

    public <T> T executePut(@NonNull String url, Class<T> payloadClass, Object body) {
        var requestBody = payloadConverter.serialize(body);

        Request request = new Request.Builder()
                .url(url)
                .put(requestBody)
                .build();
        return executeRequest(request, payloadClass, true);
    }

    public <T> T executeDelete(@NonNull String url, Class<T> payloadClass, Object body) {
        if (body != null) {
            var requestBody = payloadConverter.serialize(body);

            Request request = new Request.Builder()
                    .url(url)
                    .delete(requestBody)
                    .build();
            return executeRequest(request, payloadClass, true);
        } else {
            Request request = new Request.Builder()
                    .url(url)
                    .delete()
                    .build();
            return executeRequest(request, payloadClass, true);
        }
    }

    public String buildUrl(Object... args) {
        checkConfig();
        StringBuilder url = new StringBuilder(config.getUrl());

        for (var item : args) {
            if (item instanceof String s) {
                url.append("/").append(s);
            } else {
                url.append("/").append(item);
            }
        }

        return url.toString();
    }

}
