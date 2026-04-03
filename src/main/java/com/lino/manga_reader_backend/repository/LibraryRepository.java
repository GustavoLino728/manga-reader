package com.lino.manga_reader_backend.repository;

import com.lino.manga_reader_backend.domain.LibraryEntry;
import com.lino.manga_reader_backend.domain.enums.LibraryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LibraryRepository extends JpaRepository<LibraryEntry, UUID> {

    List<LibraryEntry> findAllByUserId(UUID userId);

    List<LibraryEntry> findAllByUserIdAndStatus(UUID userId, LibraryStatus status);

    Optional<LibraryEntry> findByUserIdAndMangaId(UUID userId, String mangaId);

    boolean existsByUserIdAndMangaId(UUID userId, String mangaId);

    void deleteByUserIdAndMangaId(UUID userId, String mangaId);
}