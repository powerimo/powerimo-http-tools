package org.powerimo.http.exceptions;

public class ApiClientException extends RuntimeException {

    public ApiClientException() {
        super();
    }

    public ApiClientException(String message) {
        super(message);
    }

    public ApiClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiClientException(Throwable cause) {
        super(cause);
    }
}
