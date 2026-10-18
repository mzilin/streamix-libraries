package com.mariuszilinskas.streamix.web.exception;

/**
 * This class represents a custom exception thrown when an operation would exceed
 * the allowed limit for a particular entity.
 *
 * @author Marius Zilinskas
 */
public class EntityLimitExceededException extends RuntimeException {

    public EntityLimitExceededException(String entityName, int limit) {
        super("A user cannot have more than %d %s".formatted(limit, entityName));
    }

}
