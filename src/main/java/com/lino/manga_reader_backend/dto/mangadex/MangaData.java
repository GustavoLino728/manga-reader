package com.lino.manga_reader_backend.dto.mangadex;

import java.util.List;
import java.util.Map;

public record MangaData(
    String id,
    MangaAttributes attributes,
    List<Relationship> relationships
) {
    public record MangaAttributes(
        Map<String, String> title,
        Map<String, String> description,
        String status,
        Integer year,
        String contentRating
    ) {}

    public record Relationship(
        String id,
        String type,
        Map<String, Object> attributes
    ) {}
}