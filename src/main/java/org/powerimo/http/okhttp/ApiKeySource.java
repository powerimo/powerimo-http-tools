package org.powerimo.http.okhttp;

import okhttp3.Request;

public interface ApiKeySource {
    String getApiKeyForRequest(Request request);
}
