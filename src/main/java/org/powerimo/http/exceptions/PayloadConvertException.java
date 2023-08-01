package org.powerimo.http.exceptions;

public class PayloadConvertException extends RuntimeException {

    public PayloadConvertException() {
        super();
    }

    public PayloadConvertException(String message) {
        super(message);
    }

    public PayloadConvertException(String message, Throwable cause) {
        super(message, cause);
    }

    public PayloadConvertException(Throwable cause) {
        super(cause);
    }

    protected PayloadConvertException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
