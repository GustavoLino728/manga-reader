package com.lino.manga_reader_backend.dto.mangadex;

public record MangaDexSingleResponse<T>(
    String result,
    T data
) {}