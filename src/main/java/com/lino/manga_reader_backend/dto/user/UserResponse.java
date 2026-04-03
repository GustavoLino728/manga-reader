package com.lino.manga_reader_backend.dto.user;

import com.lino.manga_reader_backend.domain.User;

import java.time.Instant;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String email,
        String preferredLang,
        String imageQuality,
        String readerDirection,
        Instant createdAt
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPreferredLang(),
                user.getImageQuality(),
                user.getReaderDirection(),
                user.getCreatedAt()
        );
    }
}
