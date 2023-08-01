package org.powerimo.http.exceptions;

import lombok.Getter;

@Getter
public class ApiCallException extends RuntimeException {
    private final int code;

    public static String formatMessage(int code, String message) {
        return "API returned response code " + code + " with the message '" + message + "'";
    }

    public ApiCallException(int code, String message) {
        super(formatMessage(code, message));
        this.code = code;
    }

}
