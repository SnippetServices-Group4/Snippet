package com.services.group4.snippet.services.async;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class FinalFormatEventProducer {
  private final String streamKey;
  private final RedisTemplate<String, String> redis;
  private final ObjectMapper mapper = new ObjectMapper();

  @Autowired
  public FinalFormatEventProducer(
      @Value("${stream.final.format.key}") String streamKey,
      @NotNull RedisTemplate<String, String> redis) {
    this.streamKey = streamKey;
    this.redis = redis;
  }

  public void emit(String jsonMessage) {
    ObjectRecord<String, String> result =
        StreamRecords.newRecord().ofObject(jsonMessage).withStreamKey(streamKey);

    redis.opsForStream().add(result);
  }

  public void publishEvent(Map<String, Object> messageMap) {
    String request = null;
    try {
      request = mapper.writeValueAsString(messageMap);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

    System.out.println("Publishing final format event: " + request);
    emit(request);
  }
}