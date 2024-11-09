package com.services.group4.snippet.dto;

public record ResponseDto<T>(String message, T data) {
}