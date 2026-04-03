package com.lino.manga_reader_backend.dto.progress;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record SaveProgressRequest(
    @NotBlank(message = "chapterId é obrigatório")
    String chapterId,

    @Min(value = 0, message = "page não pode ser negativo")
    int page
) {}