package com.services.group4.snippet.dto;

import lombok.Data;
import lombok.Generated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Generated
@Data
public class SnippetDto {

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Content is required")
    private String content;

    @NotNull(message = "Owner is required")
    private Long owner;

    public SnippetDto() {}

    public SnippetDto(String name, String content, Long owner) {
        this.name = name;
        this.content = content;
        this.owner = owner;
    }
}