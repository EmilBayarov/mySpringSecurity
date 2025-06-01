package org.example.myspringsecurity.model.repository;

import org.example.myspringsecurity.model.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, String> {
    @Query("""
            select token
            from Token token
            where token.revoked=false
            and token.expired=false
            and token.user.id=:userId
            """)
    List<Token> findAllValidTokensByUser(Integer userId);

    @Query("""
            select token
            from Token token
            where token.revoked=false
            and token.expired=false
            and token.refreshToken=:token
            """)
    Optional<Token> findByToken(String token);
}
