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
    public String apiKey;

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getApiKey() {
        return apiKey;
    }

}
