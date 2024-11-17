package com.services.group4.snippet.dto.snippet.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.services.group4.snippet.common.DataTuple;
import com.services.group4.snippet.common.json.ResponseDtoDeserializer;
import com.services.group4.snippet.common.json.ResponseDtoSerializer;

@JsonSerialize(using = ResponseDtoSerializer.class)
@JsonDeserialize(using = ResponseDtoDeserializer.class)
public record ResponseDto<T>(String message, DataTuple<T> data) {}
