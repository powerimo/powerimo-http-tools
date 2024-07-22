package org.powerimo.http.exceptions;

import lombok.Getter;

@Getter
public class ApiCallException extends HttpStatusException {
    private final String apiMessageCode;
    private final String responseBody;

    public static String formatMessage(int code, String message) {
        return "API returned response code " + code + " with the message '" + message + "'";
    }

    public ApiCallException(int code, String message) {
        super(code, formatMessage(code, message));
        apiMessageCode = null;
        this.responseBody = null;
    }

    public ApiCallException(int code, String message, Throwable ex) {
        super(code, formatMessage(code, message), ex);
        apiMessageCode = null;
        this.responseBody = null;
    }

    public ApiCallException(int httpCode, String apiMessageCode, String apiMessage, String responseBody) {
        super(httpCode, apiMessage);
        this.apiMessageCode = apiMessageCode;
        this.responseBody = responseBody;
    }

}
