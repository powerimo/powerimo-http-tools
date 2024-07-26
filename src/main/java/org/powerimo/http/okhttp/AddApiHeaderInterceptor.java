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

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        var request = chain.request();

        var authorizedRequest = request.newBuilder()
                .addHeader(apiKeyHeader, apiKey != null ? apiKey : "")
                .build();

        return chain.proceed(authorizedRequest);
    }
}
