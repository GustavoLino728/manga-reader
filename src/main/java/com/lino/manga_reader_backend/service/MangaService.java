package com.lino.manga_reader_backend.service;

import com.lino.manga_reader_backend.dto.manga.ChapterResponse;
import com.lino.manga_reader_backend.dto.manga.MangaResponse;
import com.lino.manga_reader_backend.dto.manga.MangaSearchResponse;
import com.lino.manga_reader_backend.dto.mangadex.ChapterData;
import com.lino.manga_reader_backend.dto.mangadex.MangaData;
import com.lino.manga_reader_backend.dto.mangadex.MangaDexPagedResponse;
import com.lino.manga_reader_backend.dto.mangadex.MangaDexSingleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MangaService {

    private final WebClient mangaDexClient;

    public MangaSearchResponse search(String query, String lang, int limit, int offset) {
        var response = mangaDexClient.get()
            .uri(uri -> uri.path("/manga")
                .queryParam("title", query)
                .queryParam("limit", limit)
                .queryParam("offset", offset)
                .queryParam("availableTranslatedLanguage[]", lang)
                .queryParam("includes[]", "cover_art")
                .queryParam("order[relevance]", "desc")
                .queryParam("contentRating[]", "safe")
                .queryParam("contentRating[]", "suggestive")
                .build())
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<MangaDexPagedResponse<MangaData>>() {})
            .block();

        if (response == null || !"ok".equals(response.result()))
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Erro ao consultar MangaDex");

        var results = response.data().stream()
            .map(d -> MangaResponse.from(d, lang))
            .toList();

        return new MangaSearchResponse(results, response.total(), response.offset(), response.limit());
    }

    public MangaResponse getById(String mangaId, String lang) {
        var response = mangaDexClient.get()
            .uri(uri -> uri.path("/manga/{id}")
                .queryParam("includes[]", "cover_art")
                .queryParam("includes[]", "author")
                .build(mangaId))
            .retrieve()
            .onStatus(status -> status.value() == 404,
                r -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Manga não encontrado")))
            .bodyToMono(new ParameterizedTypeReference<MangaDexSingleResponse<MangaData>>() {})
            .block();

        if (response == null)
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Erro ao consultar MangaDex");

        return MangaResponse.from(response.data(), lang);
    }

    public List<ChapterResponse> getChapters(String mangaId, String lang, int limit, int offset) {
        var response = mangaDexClient.get()
            .uri(uri -> uri.path("/manga/{id}/feed")
                .queryParam("translatedLanguage[]", lang)
                .queryParam("limit", limit)
                .queryParam("offset", offset)
                .queryParam("order[volume]", "asc")
                .queryParam("order[chapter]", "asc")
                .queryParam("includes[]", "scanlation_group")
                .build(mangaId))
            .retrieve()
            .onStatus(status -> status.value() == 404,
                r -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Manga não encontrado")))
            .bodyToMono(new ParameterizedTypeReference<MangaDexPagedResponse<ChapterData>>() {})
            .block();

        if (response == null)
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Erro ao consultar MangaDex");

        return response.data().stream()
            .map(ChapterResponse::from)
            .toList();
    }
}