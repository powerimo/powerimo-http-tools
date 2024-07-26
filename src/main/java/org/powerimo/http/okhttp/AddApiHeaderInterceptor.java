package org.powerimo.http.okhttp;

import lombok.Getter;
import lombok.Setter;
import okhttp3.Interceptor;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

@Getter
@Setter
public class AddApiHeaderInterceptor implements Interceptor {
    private String apiKeyHeader = "x-api-key";
    private String apiKey;
    private ApiKeySource apiKeySource;

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        var request = chain.request();

        String actualApiKey = apiKey;
        if (apiKeySource != null) {
            actualApiKey = apiKeySource.getApiKeyForRequest(request);
        }

        var authorizedRequest = request.newBuilder()
                .addHeader(apiKeyHeader, actualApiKey != null ? actualApiKey : "")
                .build();

        return chain.proceed(authorizedRequest);
    }
}
