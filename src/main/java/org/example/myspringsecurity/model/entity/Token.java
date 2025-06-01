package org.example.myspringsecurity.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "token")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private boolean expired;
    private boolean revoked;
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;
    private String refreshToken;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
