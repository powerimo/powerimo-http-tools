package org.powerimo.http.exceptions;

import lombok.Getter;

@Getter
public class ApiCallException extends RuntimeException {
    private final int code;
    private final String apiMessageCode;
    private final String responseBody;

    public static String formatMessage(int code, String message) {
        return "API returned response code " + code + " with the message '" + message + "'";
    }

    public ApiCallException(int code, String message) {
        super(formatMessage(code, message));
        this.code = code;
        apiMessageCode = null;
        this.responseBody = null;
    }

    public ApiCallException(int code, String message, Throwable ex) {
        super(formatMessage(code, message), ex);
        this.code = code;
        apiMessageCode = null;
        this.responseBody = null;
    }

    public ApiCallException(int httpCode, String apiMessageCode, String apiMessage, String responseBody) {
        super(apiMessage);
        this.code = httpCode;
        this.apiMessageCode = apiMessageCode;
        this.responseBody = responseBody;
    }

}
