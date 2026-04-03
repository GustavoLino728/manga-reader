package com.lino.manga_reader_backend.service;

import com.lino.manga_reader_backend.domain.ReadingProgress;
import com.lino.manga_reader_backend.domain.User;
import com.lino.manga_reader_backend.dto.progress.ProgressResponse;
import com.lino.manga_reader_backend.dto.progress.SaveProgressRequest;
import com.lino.manga_reader_backend.repository.ReadingProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProgressService {

    private final ReadingProgressRepository progressRepository;

    public ProgressResponse getProgress(UUID userId, String mangaId) {
        return progressRepository.findByUserIdAndMangaId(userId, mangaId)
            .map(ProgressResponse::from)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Nenhum progresso registrado para esse manga"));
    }

    @Transactional
    public ProgressResponse saveProgress(UUID userId, User user, String mangaId, SaveProgressRequest req) {
        var progress = progressRepository
            .findByUserIdAndMangaId(userId, mangaId)
            .orElseGet(() -> ReadingProgress.builder()
                .user(user)
                .mangaId(mangaId)
                .build());

        progress.setChapterId(req.chapterId());
        progress.setPage(req.page());

        return ProgressResponse.from(progressRepository.save(progress));
    }
}