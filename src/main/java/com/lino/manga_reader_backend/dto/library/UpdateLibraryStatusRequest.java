package com.lino.manga_reader_backend.dto.library;

import com.lino.manga_reader_backend.domain.enums.LibraryStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateLibraryStatusRequest(
    @NotNull(message = "status é obrigatório")
    LibraryStatus status
) {}