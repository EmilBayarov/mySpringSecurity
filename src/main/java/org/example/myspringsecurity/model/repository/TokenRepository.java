package org.example.myspringsecurity.model.repository;

import org.example.myspringsecurity.model.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TokenRepository extends JpaRepository<Token, Integer> {
    @Query("""
            select token
            from Token token
            where token.revoked=false
            and token.expired=false
            and token.user.id=:userId
            """)
    List<Token> findAllValidTokensByUser(Integer userId);
}
