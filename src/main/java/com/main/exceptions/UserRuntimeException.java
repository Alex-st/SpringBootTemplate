package com.main.exceptions;

/**
 * Created by Oleksandr on 10/10/2016.
 */
public class UserRuntimeException extends RuntimeException {

    public UserRuntimeException(String message) {
        super(message);
    }

    public UserRuntimeException(Throwable cause) {
        super(cause);
    }
}
