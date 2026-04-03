package com.lino.manga_reader_backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record AppProperties(
        Jwt jwt,
        Mangadex mangadex
) {
    public record Jwt(
            String secret,
            long expirationMs,
            long refreshExpirationMs
    ) {}

    public record Mangadex(
            String baseUrl,
            int atHomeCacheTtlMinutes
    ) {}
}