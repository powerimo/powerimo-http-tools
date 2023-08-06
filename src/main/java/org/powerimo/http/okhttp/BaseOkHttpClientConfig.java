package org.powerimo.http.okhttp;

public interface BaseOkHttpClientConfig {
    String getUrl();
    String getApiKey();
    long getConnectTimeout();
    long getCallTimeout();
}
