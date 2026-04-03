package com.lino.manga_reader_backend.controller;

import com.lino.manga_reader_backend.dto.manga.ChapterResponse;
import com.lino.manga_reader_backend.dto.manga.MangaResponse;
import com.lino.manga_reader_backend.dto.manga.MangaSearchResponse;
import com.lino.manga_reader_backend.service.MangaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/manga")
@RequiredArgsConstructor
@Tag(name = "Manga", description = "Busca e detalhes de mangas via MangaDex")
public class MangaController {

    private final MangaService mangaService;

    @Operation(summary = "Busca mangas por título")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/search")
    public ResponseEntity<MangaSearchResponse> search(
            @RequestParam String q,
            @RequestParam(defaultValue = "pt-br") String lang,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "0") int offset) {
        return ResponseEntity.ok(mangaService.search(q, lang, limit, offset));
    }

    @Operation(summary = "Detalhes de um manga pelo ID do MangaDex")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{mangaId}")
    public ResponseEntity<MangaResponse> getById(
            @PathVariable String mangaId,
            @RequestParam(defaultValue = "pt-br") String lang) {
        return ResponseEntity.ok(mangaService.getById(mangaId, lang));
    }

    @Operation(summary = "Lista capítulos de um manga")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{mangaId}/chapters")
    public ResponseEntity<List<ChapterResponse>> getChapters(
            @PathVariable String mangaId,
            @RequestParam(defaultValue = "pt-br") String lang,
            @RequestParam(defaultValue = "100") int limit,
            @RequestParam(defaultValue = "0") int offset) {
        return ResponseEntity.ok(mangaService.getChapters(mangaId, lang, limit, offset));
    }
}