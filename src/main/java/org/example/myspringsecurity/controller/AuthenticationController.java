package org.example.myspringsecurity.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.myspringsecurity.model.dto.AuthenticationRequest;
import org.example.myspringsecurity.model.dto.AuthenticationResponse;
import org.example.myspringsecurity.model.dto.RegisterRequest;
import org.example.myspringsecurity.service.AuthenticationService;
import org.example.myspringsecurity.service.CookieService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final CookieService cookieService;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody @Valid RegisterRequest registerRequest
    ) {
        authenticationService.register(registerRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody @Valid AuthenticationRequest authenticationRequest
    ) {
        AuthenticationResponse authResponse = authenticationService.authenticate(authenticationRequest);
        ResponseCookie cookie = cookieService.createRefreshTokenCookie(authResponse.refreshToken());
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(authResponse);
    }

    @GetMapping("/confirm")
    public void confirm(
            @RequestParam("code") String code
    ) {
        authenticationService.activateAccount(code);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refresh(
            @CookieValue(CookieService.COOKIE_NAME) final String refreshToken
    ) {
        System.out.println("Refresh token:: "+refreshToken);
        AuthenticationResponse authResponse =authenticationService.refresh(refreshToken);
        ResponseCookie cookie = cookieService.createRefreshTokenCookie(authResponse.refreshToken());
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(authResponse);
    }

    @GetMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        authenticationService.refreshToken(request,response);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Void> deAuthenticate(
            @CookieValue(CookieService.COOKIE_NAME) final String refreshToken
    ) {
        authenticationService.deAuthenticate(refreshToken);
        ResponseCookie cookie = cookieService.createLogoutCookie();
        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @DeleteMapping("/all")
    public ResponseEntity<Void> deAuthenticateFromAllSession() {
        authenticationService.deAuthenticateFromAllSession();
        ResponseCookie cookie = cookieService.createLogoutCookie();
        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Tested endpoint");
    }
}
