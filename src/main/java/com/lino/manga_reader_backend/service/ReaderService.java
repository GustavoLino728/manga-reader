package com.lino.manga_reader_backend.service;

import com.lino.manga_reader_backend.dto.mangadex.AtHomeResponse;
import com.lino.manga_reader_backend.dto.reader.CachedChapterPages;
import com.lino.manga_reader_backend.dto.reader.ReaderSessionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class ReaderService {

    private final WebClient mangaDexClient;
    private final WebClient imageClient;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final Duration CACHE_TTL = Duration.ofMinutes(14);
    private static final String CACHE_PREFIX = "reader:chapter:";

    public ReaderSessionResponse getSession(String chapterId, String quality) {
        var cached = getOrFetchPages(chapterId);

        List<String> sourcePages = "data-saver".equals(quality)
            ? cached.dataSaver()
            : cached.data();

        List<String> proxyUrls = IntStream.range(0, sourcePages.size())
            .mapToObj(i -> "/reader/" + chapterId + "/page/" + i)
            .toList();

        return new ReaderSessionResponse(chapterId, sourcePages.size(), proxyUrls);
    }

    public byte[] getPage(String chapterId, int pageIndex, String quality) {
        var cached = getOrFetchPages(chapterId);

        List<String> pages = "data-saver".equals(quality)
                ? cached.dataSaver()
                : cached.data();

        if (pageIndex < 0 || pageIndex >= pages.size())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Página " + pageIndex + " não existe nesse capítulo");

        String folder = "data-saver".equals(quality) ? "data-saver" : "data";
        String imageUrl = cached.baseUrl() + "/" + folder + "/" + cached.hash() + "/" + pages.get(pageIndex);

        return imageClient.get()
                .uri(imageUrl)
                .retrieve()
                .bodyToMono(byte[].class)
                .block();
    }

    private CachedChapterPages getOrFetchPages(String chapterId) {
        String key = CACHE_PREFIX + chapterId;

        var cached = redisTemplate.opsForValue().get(key);
        if (cached instanceof CachedChapterPages pages) return pages;

        var response = mangaDexClient.get()
            .uri("/at-home/server/{id}", chapterId)
            .retrieve()
            .onStatus(status -> status.value() == 404,
                r -> Mono.error(new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Capítulo não encontrado")))
            .bodyToMono(AtHomeResponse.class)
            .block();

        if (response == null || !"ok".equals(response.result()))
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                "Erro ao obter capítulo do MangaDex");

        var result = new CachedChapterPages(
            response.baseUrl(),
            response.chapter().hash(),
            response.chapter().data(),
            response.chapter().dataSaver()
        );

        redisTemplate.opsForValue().set(key, result, CACHE_TTL);
        return result;
    }
}