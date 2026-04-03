package com.lino.manga_reader_backend.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
        @Size(min = 2, max = 100) String name,
        @Email String email,

        @Pattern(regexp = "^(pt|en|es|ja)$",
                message = "preferredLang deve ser: pt, en, es ou ja")
        String preferredLang,

        @Pattern(regexp = "^(data|data-saver)$",
                message = "imageQuality deve ser: data ou data-saver")
        String imageQuality,

        @Pattern(regexp = "^(vertical|horizontal)$",
                message = "readerDirection deve ser: vertical ou horizontal")
        String readerDirection
) {}
