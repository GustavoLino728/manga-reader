package com.lino.manga_reader_backend.dto.reader;

import java.io.Serializable;
import java.util.List;

public record CachedChapterPages(
    String baseUrl,
    String hash,
    List<String> data,
    List<String> dataSaver
) implements Serializable {}