package com.lino.manga_reader_backend.controller;

import com.lino.manga_reader_backend.dto.reader.ReaderSessionResponse;
import com.lino.manga_reader_backend.service.ReaderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reader")
@RequiredArgsConstructor
@Tag(name = "Reader", description = "Leitor de capítulos — proxy de páginas via MangaDex")
public class ReaderController {

    private final ReaderService readerService;

    @Operation(summary = "Inicia sessão de leitura — retorna total de páginas e URLs proxiadas")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{chapterId}")
    public ResponseEntity<ReaderSessionResponse> getSession(
            @PathVariable String chapterId,
            @RequestParam(defaultValue = "data") String quality) {
        return ResponseEntity.ok(readerService.getSession(chapterId, quality));
    }

    @Operation(summary = "Retorna a imagem de uma página pelo índice")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{chapterId}/page/{pageIndex}")
    public ResponseEntity<byte[]> getPage(
            @PathVariable String chapterId,
            @PathVariable int pageIndex,
            @RequestParam(defaultValue = "data") String quality) {
        byte[] image = readerService.getPage(chapterId, pageIndex, quality);
        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_JPEG)
            .body(image);
    }
}