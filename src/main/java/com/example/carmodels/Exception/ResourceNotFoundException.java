package com.example.carmodels.Exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.example.carmodels.Constants.ErrorConst.RESOURCE_NOT_FOUND;

/**
 * Custom exception class representing resource not found situations.
 * This exception is mapped to HTTP status code 404 - Not Found.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructs a new ResourceNotFoundException with a default message.
     */
    public ResourceNotFoundException() {
        super(RESOURCE_NOT_FOUND);
        Logger logger = LoggerFactory.getLogger(ResourceNotFoundException.class);
        logger.error(RESOURCE_NOT_FOUND, this);
    }
}

