package com.lino.manga_reader_backend.dto.mangadex;

import java.util.List;

public record MangaDexPagedResponse<T>(
    String result,
    List<T> data,
    int limit,
    int offset,
    int total
) {}