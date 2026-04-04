package com.lino.manga_reader_backend.dto.mangadex;

import java.util.List;

public record AtHomeResponse(
    String result,
    String baseUrl,
    ChapterInfo chapter
) {
    public record ChapterInfo(
        String hash,
        List<String> data,
        List<String> dataSaver
    ) {}
}