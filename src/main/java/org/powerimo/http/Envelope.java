package org.powerimo.http;

import lombok.Data;

import java.time.Instant;

/**
 * A generic container used to encapsulate data, metadata, and status information
 * for API responses or other data structures. The class provides utility methods
 * for creating instances with a fluent builder pattern for customization.
 *
 * @param <T> the type of the data contained within the envelope
 */
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

    public static <T> Envelope<T> of(T payload, String path) {
        Envelope<T> envelope = new Envelope<>();
        envelope.code = 200;
        envelope.data = payload;
        envelope.timestamp = Instant.now();
        envelope.path = path;
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

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static <F> Builder<F> builderByClass(Class<F> payloadClass) {
        return new Builder<F>();
    }

    public static class Builder<T> {
        private Instant timestamp;
        private Integer code;
        private T data;
        private String message;
        private String messageCode;
        private String path;

        public Builder<T> timestamp(Instant value) {
            this.timestamp = value;
            return this;
        }

        public Builder<T> code(Integer code) {
            this.code = code;
            return this;
        }

        public Builder<T> data(T data) {
            this.data = data;
            return this;
        }

        public Builder<T> message(String message) {
            this.message = message;
            return this;
        }

        public Builder<T> messageCode(String value) {
            this.messageCode = value;
            return this;
        }

        public Builder<T> path(String value) {
            this.path = value;
            return this;
        }

        public Envelope<T> build() {
            Envelope<T> envelope = new Envelope<T>();
            if (this.code != null)
                envelope.code = this.code;
            if (this.timestamp != null)
                envelope.timestamp = this.timestamp;
            else
                envelope.timestamp = Instant.now();
            envelope.message = this.message;
            envelope.messageCode = this.messageCode;
            envelope.path = this.path;
            envelope.data = this.data;
            return envelope;
        }
    }
}
