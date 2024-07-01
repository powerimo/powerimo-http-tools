package org.powerimo.http.exceptions;

public class TokenVerifyException extends ApiCallException {

    public TokenVerifyException(int code, String message) {
        super(code, message);
    }

    public TokenVerifyException(int code, String message, Throwable ex) {
        super(code, message, ex);
    }

    public TokenVerifyException(int httpCode, String apiMessageCode, String apiMessage, String responseBody) {
        super(httpCode, apiMessageCode, apiMessage, responseBody);
    }

}
