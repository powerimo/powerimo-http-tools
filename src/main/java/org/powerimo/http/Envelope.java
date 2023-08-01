package org.powerimo.http;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;

import java.time.Instant;

@Data
public class Envelope<T> {
    private Instant timestamp;
    private int code;
    private T data;
    private String message;
    private String messageCode;
    private String path;

    public static <T> Envelope<T> of(T payload) {
        Envelope<T> envelope = new Envelope<>();
        envelope.code = 200;
        envelope.data = payload;
        envelope.timestamp = Instant.now();
        return envelope;
    }

    public static <T> Envelope<T> of(T payload, HttpServletRequest request) {
        Envelope<T> envelope = new Envelope<>();
        envelope.code = 200;
        envelope.data = payload;
        envelope.timestamp = Instant.now();
        if (request != null) {
            envelope.path = request.getRequestURI();
        }
        return envelope;
    }

    public static <T> Envelope<T> error(T payload, int code, String message) {
        Envelope<T> envelope = new Envelope<>();
        envelope.code = code;
        envelope.data = payload;
        envelope.timestamp = Instant.now();
        envelope.message = message;
        return envelope;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder<T> {
        private Instant timestamp;
        private Integer code;
        private T data;
        private String message;
        private String messageCode;
        private String path;
        private HttpServletRequest request;

        public Builder timestamp(Instant value) {
            this.timestamp = value;
            return this;
        }

        public Builder code(Integer code) {
            this.code = code;
            return this;
        }

        public Builder data(T data) {
            this.data = data;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder messageCode(String value) {
            this.messageCode = value;
            return this;
        }

        public Builder request(HttpServletRequest request) {
            this.request = request;
            return this;
        }

        public Builder path(String value) {
            this.path = value;
            return this;
        }

        public Envelope<T> build() {
            Envelope<T> envelope = new Envelope<>();
            if (this.code != null)
                envelope.code = this.code;
            if (this.timestamp != null)
                envelope.timestamp = this.timestamp;
            else
                envelope.timestamp = Instant.now();
            envelope.message = this.message;
            envelope.messageCode = this.messageCode;
            if (this.request != null && this.path == null)
                envelope.path = this.request.getRequestURI();
            else
                envelope.path = this.path;
            envelope.data = this.data;
            return envelope;
        }
    }
}
