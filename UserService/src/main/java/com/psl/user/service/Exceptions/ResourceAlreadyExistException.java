package com.psl.user.service.Exceptions;

public class ResourceAlreadyExistException extends RuntimeException {

    public ResourceAlreadyExistException() {
        super("This resource is already exist");
    }
    public ResourceAlreadyExistException(String s) {
        super(s);
    }
}
