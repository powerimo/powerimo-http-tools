package org.powerimo.http.okhttp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaseOkHttpApiClientLocalConfig implements BaseOkHttpClientConfig {
    private String url;
    private String apiKey;
    private long connectTimeout = 0;
    private long callTimeout = 0;

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getApiKey() {
        return apiKey;
    }

    @Override
    public long getConnectTimeout() {
        return connectTimeout;
    }

    @Override
    public long getCallTimeout() {
        return callTimeout;
    }
}
