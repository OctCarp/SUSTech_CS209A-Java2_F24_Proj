package io.github.octcarp.sustech.cs209a.proj.apijava.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
