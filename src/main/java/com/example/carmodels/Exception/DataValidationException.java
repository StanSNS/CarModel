package com.example.carmodels.Exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.example.carmodels.Constants.ErrorConst.VALIDATION_FAILED;

/**
 * Custom exception class for data validation errors.
 * This exception is thrown when there is a failure in data validation.
 */
@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class DataValidationException extends RuntimeException {

    /**
     * Constructs a new DataValidationException with a default error message.
     * Also logs the exception.
     */
    public DataValidationException() {
        super(VALIDATION_FAILED);
        Logger logger = LoggerFactory.getLogger(DataValidationException.class);
        logger.error(VALIDATION_FAILED, this);
    }
}
