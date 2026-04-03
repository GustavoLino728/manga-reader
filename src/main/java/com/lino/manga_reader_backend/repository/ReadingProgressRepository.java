package com.lino.manga_reader_backend.repository;

import com.lino.manga_reader_backend.domain.ReadingProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ReadingProgressRepository extends JpaRepository<ReadingProgress, UUID> {

    Optional<ReadingProgress> findByUserIdAndMangaId(UUID userId, String mangaId);
}