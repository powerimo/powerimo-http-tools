package org.powerimo.http.exceptions;

import lombok.Getter;

@Getter
public class AccessTokenObtainFailed extends RuntimeException {
    private final String details;
    private static final String DETAILED_MESSAGE = "%s. Details: %s";

    public AccessTokenObtainFailed(String message, String details, Throwable cause) {
        super(String.format(DETAILED_MESSAGE, message, details), cause);
        this.details = details;
    }

    public AccessTokenObtainFailed(String message, String details) {
        super(String.format(DETAILED_MESSAGE, message, details));
        this.details = details;
    }

}
