package org.powerimo.http.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpStatusExceptionTest {

    @Test
    void test_getHttpCode() {
        var ex = new HttpStatusException(403, "Forbidden");
        assertEquals(403, ex.getHttpCode());
        assertEquals("Forbidden", ex.getMessage());

        assertThrows(HttpStatusException.class, () -> {throw ex;}, "Forbidden" );
        assertThrows(HttpStatusException.class, () -> {throw new HttpStatusException(404, "External message", ex);}, "External message" );
    }
}