package com.services.group4.snippet.common.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.group4.snippet.common.DataTuple;
import com.services.group4.snippet.dto.ResponseDto;

import java.io.IOException;

public class ResponseDtoDeserializer<T> extends JsonDeserializer<ResponseDto<T>> {

  @Override
  public ResponseDto<T> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    ObjectMapper mapper = (ObjectMapper) p.getCodec();
    JsonNode rootNode = mapper.readTree(p);

    String message = rootNode.get("message").asText();
    JsonNode dataNode = rootNode.get("data");

    DataTuple<T> dataTuple;
    if (dataNode.has("name") && dataNode.has("data")) {
      // Es un DataTuple
      String name = dataNode.get("name").asText();
      T data = mapper.convertValue(dataNode.get("data"), ctxt.getTypeFactory().constructType(Object.class));
      dataTuple = new DataTuple<>(name, data);
    } else {
      // Es una estructura simple
      String name = dataNode.fieldNames().next();
      T data = mapper.convertValue(dataNode.get(name), ctxt.getTypeFactory().constructType(Object.class));
      dataTuple = new DataTuple<>(name, data);
    }

    return new ResponseDto<>(message, dataTuple);
  }
}
