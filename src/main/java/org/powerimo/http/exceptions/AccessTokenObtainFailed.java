package org.powerimo.http.exceptions;

public class AccessTokenObtainFailed extends RuntimeException {
    private final String details;

    public AccessTokenObtainFailed(String message, String details, Throwable cause) {
        super(message, cause);
        this.details = details;
    }

    public AccessTokenObtainFailed(String message, String details) {
        super(message);
        this.details = details;
    }
}
