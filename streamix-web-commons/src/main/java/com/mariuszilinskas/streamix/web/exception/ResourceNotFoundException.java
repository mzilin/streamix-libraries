package com.mariuszilinskas.streamix.web.exception;

/**
 * This class represents a custom exception thrown when an entity cannot be found
 * using the specified identifier.
 *
 * @author Marius Zilinskas
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(Class<?> entityType, String identifierName, Object identifierValue) {
        super("%s not found with %s '%s'".formatted(
                entityType.getSimpleName(), identifierName, identifierValue));
    }

}
