package org.example.myspringsecurity.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.myspringsecurity.core.email.EmailSenderUtil;
import org.example.myspringsecurity.core.security.JwtService;
import org.example.myspringsecurity.core.util.TokenUtil;
import org.example.myspringsecurity.model.dto.AuthenticationRequest;
import org.example.myspringsecurity.model.dto.AuthenticationResponse;
import org.example.myspringsecurity.model.dto.RegisterRequest;
import org.example.myspringsecurity.model.entity.ActivationCode;
import org.example.myspringsecurity.model.entity.Role;
import org.example.myspringsecurity.model.entity.Token;
import org.example.myspringsecurity.model.entity.User;
import org.example.myspringsecurity.model.repository.ActivationCodeRepository;
import org.example.myspringsecurity.model.repository.TokenRepository;
import org.example.myspringsecurity.model.repository.UserRepository;
import org.example.myspringsecurity.securityLib.authentication.auth.AuthenticationManager;
import org.example.myspringsecurity.securityLib.authentication.UsernamePasswordAuthenticationToken;
import org.example.myspringsecurity.securityLib.core.Authentication;
import org.example.myspringsecurity.securityLib.core.context.SecurityContextHolder;
import org.example.myspringsecurity.securityLib.crypto.password.PasswordEncoder;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailSenderUtil emailSenderUtil;
    private final ActivationCodeRepository activationCodeRepository;
    private final JwtService jwtService;
    private final TokenUtil tokenUtil;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;

    public void register(RegisterRequest registerRequest) {
        User user = User.builder()
                .firstname(registerRequest.firstname())
                .lastname(registerRequest.lastname())
                .email(registerRequest.email())
                .password(passwordEncoder.encode(registerRequest.password()))
                .role(Role.USER)
                .enabled(false)
                .accountLocked(false)
                .build();
        userRepository.save(user);
        emailSenderUtil.sendValidationEmail(user);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        User user = (User) authentication.getPrincipal();
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
//        tokenUtil.revokeAllUserTokens(user);
        tokenUtil.saveUserToken(user, refreshToken);
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


    public void activateAccount(String code) {
        ActivationCode activationCode = activationCodeRepository.findByCode(code)
                .orElseThrow(()->new EntityNotFoundException("Code not found"));
        if (LocalDateTime.now().isAfter(activationCode.getExpiresAt())) {
            emailSenderUtil.sendValidationEmail(activationCode.getUser());
            throw new IllegalStateException("Activation code has been expired!");
        }
        User user = userRepository.findByEmail(activationCode.getUser().getEmail())
                .orElseThrow(()->new EntityNotFoundException("User not found by email"));
        user.setEnabled(true);
        activationCode.setValidatedAt(LocalDateTime.now());
        userRepository.save(user);
        activationCodeRepository.save(activationCode);
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String userEmail;
        final String refreshToken;
        if (authHeader==null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user  = userRepository.findByEmail(userEmail)
                    .orElseThrow(()-> new RuntimeException("Entity not found"));
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                tokenUtil.revokeAllUserTokens(user);
                tokenUtil.saveUserToken(user, refreshToken);
                var authResponse = AuthenticationResponse.builder()
                        .refreshToken(refreshToken)
                        .accessToken(accessToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(),authResponse);
            }
        }
    }

    public AuthenticationResponse refresh(String token) {
        Token existingToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token: "+token));
        tokenUtil.revokeToken(existingToken);
        final String accessToken = jwtService.generateToken(existingToken.getUser());
        final String refreshToken = jwtService.generateRefreshToken(existingToken.getUser());
        tokenUtil.saveUserToken(existingToken.getUser(), refreshToken);
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void deAuthenticate(String token) {
        Token existingToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token: "+token));
        tokenUtil.revokeToken(existingToken);
        SecurityContextHolder.clearContext();
    }

    public void deAuthenticateFromAllSession() {
        System.err.println("Security Context Holder:: "+SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        tokenUtil.revokeAllUserTokens(user);
        SecurityContextHolder.clearContext();
    }
}
