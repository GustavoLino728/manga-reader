package com.lino.manga_reader_backend.controller;

import com.lino.manga_reader_backend.domain.User;
import com.lino.manga_reader_backend.domain.enums.LibraryStatus;
import com.lino.manga_reader_backend.dto.library.AddToLibraryRequest;
import com.lino.manga_reader_backend.dto.library.LibraryEntryResponse;
import com.lino.manga_reader_backend.dto.library.UpdateLibraryStatusRequest;
import com.lino.manga_reader_backend.service.LibraryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/library")
@RequiredArgsConstructor
@Tag(name = "Library", description = "Biblioteca pessoal de mangas do usuário")
public class LibraryController {

    private final LibraryService libraryService;

    @Operation(summary = "Lista a biblioteca do usuário, com filtro opcional por status")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    public ResponseEntity<List<LibraryEntryResponse>> getLibrary(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) LibraryStatus status) {
        return ResponseEntity.ok(libraryService.getLibrary(user.getId(), status));
    }

    @Operation(summary = "Adiciona um manga à biblioteca")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<LibraryEntryResponse> addToLibrary(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody AddToLibraryRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(libraryService.addToLibrary(user.getId(), user, req));
    }

    @Operation(summary = "Atualiza o status de um manga na biblioteca")
    @SecurityRequirement(name = "bearerAuth")
    @PatchMapping("/{mangaId}")
    public ResponseEntity<LibraryEntryResponse> updateStatus(
            @AuthenticationPrincipal User user,
            @PathVariable String mangaId,
            @Valid @RequestBody UpdateLibraryStatusRequest req) {
        return ResponseEntity.ok(libraryService.updateStatus(user.getId(), mangaId, req));
    }

    @Operation(summary = "Remove um manga da biblioteca")
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{mangaId}")
    public ResponseEntity<Void> removeFromLibrary(
            @AuthenticationPrincipal User user,
            @PathVariable String mangaId) {
        libraryService.removeFromLibrary(user.getId(), mangaId);
        return ResponseEntity.noContent().build();
    }
}