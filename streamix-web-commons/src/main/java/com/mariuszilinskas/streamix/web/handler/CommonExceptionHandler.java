package com.mariuszilinskas.streamix.web.handler;

import com.mariuszilinskas.streamix.web.exception.DownstreamServiceException;
import com.mariuszilinskas.streamix.web.exception.EntityExistsException;
import com.mariuszilinskas.streamix.web.exception.EntityLimitExceededException;
import com.mariuszilinskas.streamix.web.exception.ResourceNotFoundException;
import com.mariuszilinskas.streamix.web.response.error.ErrorResponse;
import com.mariuszilinskas.streamix.web.response.error.FieldErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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
                errors.putIfAbsent(error.getField(), error.getDefaultMessage())
        );

        return buildFieldErrorResponse(errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<FieldErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> errors = new LinkedHashMap<>();

        ex.getConstraintViolations().forEach(violation ->
                errors.putIfAbsent(violation.getPropertyPath().toString(), violation.getMessage())
        );

        return buildFieldErrorResponse(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleMessageNotReadable(HttpMessageNotReadableException ex) {
        return buildErrorResponse(
                "Request body is missing or contains invalid JSON.",
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingRequestParameter(MissingServletRequestParameterException ex) {
        return buildErrorResponse(
                "Required parameter '%s' is missing.".formatted(ex.getParameterName()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return buildErrorResponse(
                "Parameter '%s' has an invalid value.".formatted(ex.getName()),
                HttpStatus.BAD_REQUEST
        );
    }

    private ResponseEntity<FieldErrorResponse> buildFieldErrorResponse(Map<String, String> errors) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        logger.debug("Status: {}, Validation errors: {}", status.value(), errors);
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

    @ExceptionHandler(DownstreamServiceException.class)
    public ResponseEntity<ErrorResponse> handleDownstreamServiceException(DownstreamServiceException ex) {
        logger.error("Downstream service '{}' returned an unexpected error", ex.getServiceName(), ex);
        return buildErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<ErrorResponse> handleOptimisticLocking(
            OptimisticLockingFailureException ex
    ) {
        return buildErrorResponse(
                "The resource was modified by another request. Please retry.",
                HttpStatus.CONFLICT,
                ex
        );
    }

    // --------------------- Unexpected Exceptions ----------------

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception ex) {
        return buildErrorResponse("An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    // --------------------- Response Builders --------------------

    protected ResponseEntity<ErrorResponse> buildErrorResponse(String message, HttpStatus status) {
        return buildErrorResponse(message, status, null);
    }

    protected ResponseEntity<ErrorResponse> buildErrorResponse(String message, HttpStatus status, Exception ex) {
        logErrorMessage(status, message, ex);
        ErrorResponse errorResponse = new ErrorResponse(message, status.value(), status.getReasonPhrase());
        return ResponseEntity.status(status).body(errorResponse);
    }

    // --------------------- Logging ------------------------------

    private void logErrorMessage(HttpStatus status, String message, Exception ex) {
        if (status.is5xxServerError()) {
            logger.error("Status: {}, Message: '{}'", status.value(), message, ex);
        } else {
            logger.info("Status: {}, Message: '{}'", status.value(), message);
        }
    }

}
