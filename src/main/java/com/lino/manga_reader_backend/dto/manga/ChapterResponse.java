package com.lino.manga_reader_backend.dto.manga;

import com.lino.manga_reader_backend.dto.mangadex.ChapterData;

import java.time.Instant;

public record ChapterResponse(
    String id,
    String title,
    String volume,
    String chapter,
    String translatedLanguage,
    int pages,
    Instant publishAt
) {
    public static ChapterResponse from(ChapterData data) {
        var a = data.attributes();
        return new ChapterResponse(
            data.id(), a.title(), a.volume(), a.chapter(),
            a.translatedLanguage(), a.pages(), a.publishAt()
        );
    }
}