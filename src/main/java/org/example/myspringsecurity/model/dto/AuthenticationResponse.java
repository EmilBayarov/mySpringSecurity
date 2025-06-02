package org.example.myspringsecurity.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;

@Builder
public record AuthenticationResponse(
        String accessToken,
        @JsonIgnore
        String refreshToken
) {
}