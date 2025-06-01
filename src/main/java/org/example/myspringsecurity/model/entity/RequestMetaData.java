package org.example.myspringsecurity.model.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public record RequestMetaData(
    String ip,
    String userAgent,
    String origin
) {
}
