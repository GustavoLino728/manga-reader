package com.lino.manga_reader_backend.dto.manga;

import java.util.List;

public record MangaSearchResponse(
    List<MangaResponse> results,
    int total,
    int offset,
    int limit
) {}