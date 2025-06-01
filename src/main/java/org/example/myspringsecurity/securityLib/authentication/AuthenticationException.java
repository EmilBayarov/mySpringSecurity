package org.example.myspringsecurity.securityLib.authentication;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message) {
        super(message);
    }
}
