package org.powerimo.http.exceptions;

public class HttpStatusException extends RuntimeException implements HttpStatusSupport {
    private final int httpCode;

    public HttpStatusException(int httpCode, String message) {
        super(message);
        this.httpCode = httpCode;
    }

    public HttpStatusException(int httpCode, String message, Throwable ex) {
        super(message, ex);
        this.httpCode = httpCode;
    }

    @Override
    public int getHttpCode() {
        return httpCode;
    }
}
