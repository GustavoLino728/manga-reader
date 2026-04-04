package com.lino.manga_reader_backend.service;

import com.lino.manga_reader_backend.domain.LibraryEntry;
import com.lino.manga_reader_backend.domain.User;
import com.lino.manga_reader_backend.domain.enums.LibraryStatus;
import com.lino.manga_reader_backend.dto.library.AddToLibraryRequest;
import com.lino.manga_reader_backend.dto.library.LibraryEntryResponse;
import com.lino.manga_reader_backend.dto.library.UpdateLibraryStatusRequest;
import com.lino.manga_reader_backend.repository.LibraryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LibraryService {

    private final LibraryRepository libraryRepository;
    private final MangaService mangaService;

    public List<LibraryEntryResponse> getLibrary(UUID userId, LibraryStatus status) {
        List<LibraryEntry> entries = status != null
            ? libraryRepository.findAllByUserIdAndStatus(userId, status)
            : libraryRepository.findAllByUserId(userId);

        return entries.stream().map(LibraryEntryResponse::from).toList();
    }

    @Transactional
    public LibraryEntryResponse addToLibrary(UUID userId, User user, AddToLibraryRequest req) {
        if (libraryRepository.existsByUserIdAndMangaId(userId, req.mangaId()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Manga já está na biblioteca");

        String title = null;
        String coverUrl = null;
        try {
            var manga = mangaService.getById(req.mangaId(), "pt-br");
            title = manga.title();
            coverUrl = manga.coverUrl();
        } catch (Exception e) {
        }

        var entry = LibraryEntry.builder()
                .user(user)
                .mangaId(req.mangaId())
                .status(req.status() != null ? req.status() : LibraryStatus.PLAN_TO_READ)
                .title(title)
                .coverUrl(coverUrl)
                .build();

        return LibraryEntryResponse.from(libraryRepository.save(entry));
    }

    @Transactional
    public LibraryEntryResponse updateStatus(UUID userId, String mangaId, UpdateLibraryStatusRequest req) {
        var entry = libraryRepository.findByUserIdAndMangaId(userId, mangaId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entrada não encontrada na biblioteca"));

        entry.setStatus(req.status());
        return LibraryEntryResponse.from(libraryRepository.save(entry));
    }

    @Transactional
    public void removeFromLibrary(UUID userId, String mangaId) {
        if (!libraryRepository.existsByUserIdAndMangaId(userId, mangaId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Entrada não encontrada na biblioteca");

        libraryRepository.deleteByUserIdAndMangaId(userId, mangaId);
    }
}