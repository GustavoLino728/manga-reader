package com.lino.manga_reader_backend.dto.manga;

import com.lino.manga_reader_backend.dto.mangadex.MangaData;

public record MangaResponse(
    String id,
    String title,
    String description,
    String status,
    Integer year,
    String contentRating,
    String coverUrl
) {
    public static MangaResponse from(MangaData data, String preferredLang) {
        String title = data.attributes().title().getOrDefault(
            preferredLang,
            data.attributes().title().getOrDefault("en",
            data.attributes().title().values().stream().findFirst().orElse("Sem título"))
        );

        String description = data.attributes().description() != null
            ? data.attributes().description().getOrDefault(preferredLang,
              data.attributes().description().getOrDefault("en", ""))
            : "";

        String coverUrl = data.relationships().stream()
            .filter(r -> "cover_art".equals(r.type()))
            .findFirst()
            .map(r -> {
                String fileName = (String) r.attributes().get("fileName");
                return "https://uploads.mangadex.org/covers/" + data.id() + "/" + fileName + ".512.jpg";
            })
            .orElse(null);

        return new MangaResponse(
            data.id(), title, description,
            data.attributes().status(),
            data.attributes().year(),
            data.attributes().contentRating(),
            coverUrl
        );
    }
}