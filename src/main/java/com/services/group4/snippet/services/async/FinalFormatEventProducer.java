package com.services.group4.snippet.services.async;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class FinalFormatEventProducer {
  private final String streamKey;
  private final ReactiveRedisTemplate<String, String> redis;
  private final ObjectMapper mapper = new ObjectMapper();

  @Autowired
  public FinalFormatEventProducer(
      @Value("${stream.final.format.key}") String streamKey,
      @NotNull ReactiveRedisTemplate<String, String> redis) {
    this.streamKey = streamKey;
    this.redis = redis;
  }

  public Mono<ObjectRecord<String, String>> emit(String jsonMessage) {
    ObjectRecord<String, String> result =
        StreamRecords.newRecord().ofObject(jsonMessage).withStreamKey(streamKey);

    return redis.opsForStream().add(result).thenReturn(result);
  }

  public void publishEvent(Map<String, Object> messageMap) {
    System.out.println("\nFINAL FORMAT EVENT PRODUCER\n\n");

    String request;
    try {
      request = mapper.writeValueAsString(messageMap);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

    System.out.println("Publishing final format event: " + request);
    emit(request).block();
  }
}