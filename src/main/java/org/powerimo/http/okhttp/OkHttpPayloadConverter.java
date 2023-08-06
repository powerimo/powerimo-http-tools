package org.powerimo.http.okhttp;

import lombok.NonNull;
import okhttp3.RequestBody;
import org.powerimo.http.Envelope;

public interface OkHttpPayloadConverter {
    <T> T convert(@NonNull String payload, Class<T> cls);
    RequestBody serialize(Object body);
    <T> Envelope<T> deserializeEnvelope(@NonNull String payload, Class<T> cls);
    <T> T deserialize(@NonNull String payload, Class<T> cls);
}
