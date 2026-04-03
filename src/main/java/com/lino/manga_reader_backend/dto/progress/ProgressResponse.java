package com.lino.manga_reader_backend.dto.progress;

import com.lino.manga_reader_backend.domain.ReadingProgress;

import java.time.Instant;

public record ProgressResponse(
    String mangaId,
    String chapterId,
    int page,
    Instant updatedAt
) {
    public static ProgressResponse from(ReadingProgress p) {
        return new ProgressResponse(
            p.getMangaId(),
            p.getChapterId(),
            p.getPage(),
            p.getUpdatedAt()
        );
    }
}