package org.powerimo.http.okhttp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.powerimo.http.Envelope;
import org.powerimo.http.exceptions.PayloadConvertException;

@Getter
@Setter
public class StdPayloadConverter implements OkHttpPayloadConverter {

    private final ObjectMapper objectMapper;

    public StdPayloadConverter() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_WITH_ZONE_ID, true);
    }

    public StdPayloadConverter(ObjectMapper mapper) {
        this.objectMapper = mapper;
    }

    @Override
    public <T> T convert(@NonNull String payload, Class<T> cls) {
        try {
            return objectMapper.readValue(payload, cls);
        } catch (JsonProcessingException e) {
            throw new PayloadConvertException(e);
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

    @Override
    public <T> Envelope<T> deserializeEnvelope(@NonNull String payload, Class<T> cls) {
        try {
            JavaType type = objectMapper.getTypeFactory().constructParametricType(Envelope.class, cls);
            return objectMapper.readValue(payload, type);
        } catch (JsonProcessingException e) {
            throw new PayloadConvertException(e);
        }
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

}
