package com.services.group4.snippet.dto;

import com.services.group4.snippet.common.Language;

public record snippetResponseDto(Long snippetId, String name, String owner, Language language) {}
