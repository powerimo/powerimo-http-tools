package org.powerimo.http.exceptions;

public interface HttpStatusSupport {
    int getHttpCode();
    String getMessage();
    Throwable getCause();
}
