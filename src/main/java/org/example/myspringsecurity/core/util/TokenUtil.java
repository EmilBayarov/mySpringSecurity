package org.example.myspringsecurity.core.util;

import lombok.RequiredArgsConstructor;
import org.example.myspringsecurity.model.entity.Token;
import org.example.myspringsecurity.model.entity.TokenType;
import org.example.myspringsecurity.model.entity.User;
import org.example.myspringsecurity.model.repository.TokenRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TokenUtil {
    private final TokenRepository tokenRepository;

    public void revokeAllUserTokens(User user) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());
        if (validUserTokens.isEmpty()) return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void revokeToken(Token token) {
        token.setExpired(true);
        token.setRevoked(true);
        tokenRepository.save(token);
    }

    public void saveUserToken(User savedUser, String refreshToken) {
        Token token = Token.builder()
                .user(savedUser)
                .refreshToken(refreshToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();
        tokenRepository.save(token);
    }
}
