package com.services.group4.snippet.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.services.group4.snippet.dto.ResponseDto;
import com.services.group4.snippet.model.Snippet;

import java.io.IOException;
import java.util.List;

public class ResponseDtoSerializer<T> extends JsonSerializer<ResponseDto<T>> {

  @Override
  public void serialize(ResponseDto<T> responseDto, JsonGenerator gen, SerializerProvider serializers) throws IOException {
    gen.writeStartObject();
    gen.writeStringField("message", responseDto.message());

    // Personalizar el nombre de "data" en función del tipo o algún otro criterio
    String dataFieldName = getDataFieldName(responseDto.data());
    gen.writeObjectField(dataFieldName, responseDto.data());

    gen.writeEndObject();
  }

  private String getDataFieldName(T data) {
    // Aquí puedes implementar la lógica para definir el nombre del campo.
    // Por ejemplo, puedes cambiar según la clase de "data"
    if (data instanceof String) {
      return "userId";
    } else if (data instanceof Long) {
      return "snippetId";
    } else if (data instanceof List) {
      return "listSnippet";
    } else if (data instanceof Boolean) {
      return "hasPermission";
    } else if (data instanceof Snippet) {
      return "snippet";
    }
    // Nombre por defecto si no se cumple ninguna condición
    return "data";
  }
}
