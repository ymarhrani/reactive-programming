package com.learning.reactivedataaccess.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Class<?> clazz, String idValue) {
        super("No row of " + clazz.getName() + " was found with the ID = " + idValue);
    }
}
