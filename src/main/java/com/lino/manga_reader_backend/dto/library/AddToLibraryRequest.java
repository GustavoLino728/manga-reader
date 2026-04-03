package com.lino.manga_reader_backend.dto.library;

import com.lino.manga_reader_backend.domain.enums.LibraryStatus;
import jakarta.validation.constraints.NotBlank;

public record AddToLibraryRequest(
        @NotBlank(message = "mangaId é obrigatório")
    String mangaId,

        LibraryStatus status  // opcional — default PLAN_TO_READ se null
) {}