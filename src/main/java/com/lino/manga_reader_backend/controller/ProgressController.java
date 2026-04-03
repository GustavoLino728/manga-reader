package com.lino.manga_reader_backend.controller;

import com.lino.manga_reader_backend.domain.User;
import com.lino.manga_reader_backend.dto.progress.ProgressResponse;
import com.lino.manga_reader_backend.dto.progress.SaveProgressRequest;
import com.lino.manga_reader_backend.service.ProgressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/progress")
@RequiredArgsConstructor
@Tag(name = "Progress", description = "Progresso de leitura por manga")
public class ProgressController {

    private final ProgressService progressService;

    @Operation(summary = "Retorna o último capítulo e página lidos de um manga")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{mangaId}")
    public ResponseEntity<ProgressResponse> getProgress(
            @AuthenticationPrincipal User user,
            @PathVariable String mangaId) {
        return ResponseEntity.ok(progressService.getProgress(user.getId(), mangaId));
    }

    @Operation(summary = "Salva ou atualiza o progresso de leitura (upsert)")
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{mangaId}")
    public ResponseEntity<ProgressResponse> saveProgress(
            @AuthenticationPrincipal User user,
            @PathVariable String mangaId,
            @Valid @RequestBody SaveProgressRequest req) {
        return ResponseEntity.ok(progressService.saveProgress(user.getId(), user, mangaId, req));
    }
}