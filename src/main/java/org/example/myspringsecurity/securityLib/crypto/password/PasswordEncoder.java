package org.example.myspringsecurity.securityLib.crypto.password;
/**
 * Service interface for encoding passwords
 */
public interface PasswordEncoder {
    String encode(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
}
