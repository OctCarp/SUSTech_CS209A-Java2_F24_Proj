package io.github.octcarp.sustech.cs209a.proj.apijava.exception;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
