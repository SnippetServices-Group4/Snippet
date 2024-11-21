package com.services.group4.snippet.services.async;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

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
    //    try {
    //      // Introduce a delay before publishing the message
    //      Thread.sleep(5000);
    //    } catch (InterruptedException e) {
    //      Thread.currentThread().interrupt();
    //      System.err.println("Thread was interrupted: " + e.getMessage());
    //    }

    ObjectRecord<String, String> result =
        StreamRecords.newRecord().ofObject(jsonMessage).withStreamKey(streamKey);

    redis.opsForStream().add(result);
  }

  public void publishEvent(Map<String, Object> messageMap) {
    System.out.println("\nFINAL FORMAT EVENT PRODUCER\n\n");

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
