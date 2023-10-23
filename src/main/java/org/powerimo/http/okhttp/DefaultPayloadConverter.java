package org.powerimo.http.okhttp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.powerimo.http.exceptions.PayloadConvertException;

@Getter
@Setter
public class DefaultPayloadConverter implements OkHttpPayloadConverter {
    private final ObjectMapper objectMapper;

    public DefaultPayloadConverter() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_WITH_ZONE_ID, true);
    }

    public DefaultPayloadConverter(ObjectMapper objectMapper1) {
        this.objectMapper = objectMapper1;
    }

    @Override
    public RequestBody serialize(Object body) {
        try {
            if (body == null)
                return RequestBody.create(new byte[0]);
            var b = objectMapper.writeValueAsBytes(body);
            return RequestBody.create(b, MediaType.get("application/json"));
        } catch (JsonProcessingException ex) {
            throw new PayloadConvertException(ex);
        }
    }

    @Override
    public <T> T deserialize(@NonNull String payload, Class<T> cls) {
        try {
            return objectMapper.readValue(payload, cls);
        } catch (JsonProcessingException e) {
            throw new PayloadConvertException(e);
        }
    }
}
