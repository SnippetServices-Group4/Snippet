package com.services.group4.snippet.services.async;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.group4.snippet.model.TestCase;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TestEventProducer {
  private final String streamKey;
  private final RedisTemplate<String, String> redis;
  private final ObjectMapper mapper;

  @Autowired
  public TestEventProducer(
      @Value("${stream.test.key}") String streamKey, @NotNull RedisTemplate<String, String> redis, ObjectMapper mapper) {
    this.streamKey = streamKey;
    this.redis = redis;
    this.mapper = mapper;
  }

  public void emit(String jsonMessage) {
    ObjectRecord<String, String> result =
        StreamRecords.newRecord().ofObject(jsonMessage).withStreamKey(streamKey);

    redis.opsForStream().add(result);
  }

  public void publishEvent(Long snippetId, TestCase testCase) {
    System.out.println("Publishing event for snippetId: " + snippetId);

    try {
      // Create the JSON for the `config` field
      String jsonInputs = mapper.writeValueAsString(testCase.getInputs());
      String jsonOutputs = mapper.writeValueAsString(testCase.getOutputs());

      // Construct the entire message structure
      Map<String, Object> message =
          Map.of(
              "snippetId", snippetId,
              "inputs", jsonInputs,
              "outputs", jsonOutputs);

      // Serialize the complete message to a JSON string
      String finalMessageJson = mapper.writeValueAsString(message);

      // Send the message using emit
      emit(finalMessageJson);
    } catch (Exception e) {
      System.err.println("Error serializing message: " + e.getMessage());
    }
  }
}
