package com.mariuszilinskas.streamix.web.exception;

/**
 * This class represents a custom exception to be thrown when an attempt is made
 * to create an entity that already exists.
 *
 * @author Marius Zilinskas
 */
public class EntityExistsException extends RuntimeException {

    public EntityExistsException(Class<?> entityType, String fieldName, Object fieldValue) {
        super("%s with %s '%s' already exists".formatted(
                entityType.getSimpleName(), fieldName, fieldValue));
    }

}
