package com.mariuszilinskas.streamix.web.handler;

import com.mariuszilinskas.streamix.web.exception.EntityExistsException;
import com.mariuszilinskas.streamix.web.exception.EntityLimitExceededException;
import com.mariuszilinskas.streamix.web.exception.ResourceNotFoundException;
import com.mariuszilinskas.streamix.web.response.error.ErrorResponse;
import com.mariuszilinskas.streamix.web.response.error.FieldErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public abstract class CommonExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(CommonExceptionHandler.class);

    // ----------------- Request Validations ----------------------

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<FieldErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new LinkedHashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.putIfAbsent(error.getField(), error.getDefaultMessage()));

        return buildFieldErrorResponse(errors);
    }

    private ResponseEntity<FieldErrorResponse> buildFieldErrorResponse(Map<String, String> errors) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        logger.debug("Status: {}, Message: '{}'", status.value(), errors.toString());
        FieldErrorResponse errorResponse = new FieldErrorResponse(errors, status.value(), status.getReasonPhrase());
        return ResponseEntity.status(status).body(errorResponse);
    }

    // --------------------- General ------------------------------

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<ErrorResponse> handleEntityExistsException(EntityExistsException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityLimitExceededException.class)
    public ResponseEntity<ErrorResponse> handleEntityLimitExceededException(EntityLimitExceededException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    protected ResponseEntity<ErrorResponse> buildErrorResponse(String message, HttpStatus status) {
        logErrorMessage(status, message);
        ErrorResponse errorResponse = new ErrorResponse(message, status.value(), status.getReasonPhrase());
        return ResponseEntity.status(status).body(errorResponse);
    }

    private void logErrorMessage(HttpStatus status, String message) {
        if (status.is5xxServerError()) {
            logger.error("Status: {}, Message: '{}'", status.value(), message);
        } else {
            logger.info("Status: {}, Message: '{}'", status.value(), message);
        }
    }
}