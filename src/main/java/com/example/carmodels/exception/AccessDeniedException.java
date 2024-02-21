package com.example.carmodels.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.example.carmodels.constants.ErrorConst.ACCESS_DENIED;

/**
 * Custom exception class representing access denied situations.
 * This exception is mapped to HTTP status code 403 - Forbidden.
 */
@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class AccessDeniedException extends RuntimeException {

    /**
     * Constructs a new AccessDeniedException with a default message.
     */
    public AccessDeniedException() {
        super(ACCESS_DENIED);
        Logger logger = LoggerFactory.getLogger(AccessDeniedException.class);
        logger.error(ACCESS_DENIED, this);
    }
}
