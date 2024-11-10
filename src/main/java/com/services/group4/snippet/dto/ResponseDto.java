package com.services.group4.snippet.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.services.group4.snippet.common.ResponseDtoSerializer;

@JsonSerialize(using = ResponseDtoSerializer.class)
public record ResponseDto<T>(String message, T data) {
}