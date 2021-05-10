package com.sap.sfsf.reshuffle.configuration.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidDBRowCountException extends ResponseStatusException {
    public InvalidDBRowCountException(String msg) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, msg);
    }
}
