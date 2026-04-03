package com.lino.manga_reader_backend.dto.mangadex;

import java.time.Instant;

public record ChapterData(
    String id,
    ChapterAttributes attributes
) {
    public record ChapterAttributes(
        String title,
        String volume,
        String chapter,
        String translatedLanguage,
        int pages,
        Instant publishAt
    ) {}
}