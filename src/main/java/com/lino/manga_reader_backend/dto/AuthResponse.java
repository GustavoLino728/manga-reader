package com.lino.manga_reader_backend.dto;

public record AuthResponse(
    String accessToken,
    String refreshToken
) {}