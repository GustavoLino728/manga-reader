package com.lino.manga_reader_backend.dto.reader;

import java.util.List;

public record ReaderSessionResponse(
        String chapterId,
        int totalPages,
        List<String> pages  // URLs internas do seu backend, ex: /reader/{chapterId}/page/0
) {}