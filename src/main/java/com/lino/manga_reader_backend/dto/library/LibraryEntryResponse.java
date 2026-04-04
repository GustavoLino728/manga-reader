package com.lino.manga_reader_backend.dto.library;

import com.lino.manga_reader_backend.domain.LibraryEntry;
import com.lino.manga_reader_backend.domain.enums.LibraryStatus;

import java.time.Instant;
import java.util.UUID;

public record LibraryEntryResponse(
        UUID id,
        String mangaId,
        String title,
        String coverUrl,
        LibraryStatus status,
        Instant createdAt,
        Instant updatedAt
) {
    public static LibraryEntryResponse from(LibraryEntry entry) {
        return new LibraryEntryResponse(
                entry.getId(),
                entry.getMangaId(),
                entry.getTitle(),
                entry.getCoverUrl(),
                entry.getStatus(),
                entry.getCreatedAt(),
                entry.getUpdatedAt()
        );
    }
}