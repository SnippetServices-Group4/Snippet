package com.services.group4.snippet.services.async;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.group4.snippet.common.Language;
import com.services.group4.snippet.model.TestCase;
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
public class TestEventProducer {
  private final String streamKey;
  private final ReactiveRedisTemplate<String, String> redis;
  private final ObjectMapper mapper;

  @Autowired
  public TestEventProducer(
      @Value("${stream.initial.test.key}") String streamKey,
      @NotNull ReactiveRedisTemplate<String, String> redis,
      ObjectMapper mapper) {
    this.streamKey = streamKey;
    this.redis = redis;
    this.mapper = mapper;
  }

  public Mono<ObjectRecord<String, String>> emit(String jsonMessage) {
    ObjectRecord<String, String> result =
        StreamRecords.newRecord().ofObject(jsonMessage).withStreamKey(streamKey);

    return redis.opsForStream().add(result).thenReturn(result);
  }

  public void publishEvent(Long snippetId, TestCase testCase, Language language) {
    System.out.println("\nTEST EVENT PRODUCER\n\n");

    try {
      Map<String, Object> languageMap = Map.of("name", language.getLangName(), "version", language.getVersion());
      String languageJson = mapper.writeValueAsString(languageMap);

      // Create the JSON for the `config` field
      String jsonInputs = mapper.writeValueAsString(testCase.getInputs());
      String jsonOutputs = mapper.writeValueAsString(testCase.getOutputs());

      // Construct the entire message structure
      Map<String, Object> message =
          Map.of(
              "snippetId", snippetId,
              "language", languageJson,
              "testId", testCase.getId(),
              "inputs", jsonInputs,
              "outputs", jsonOutputs);

      // Serialize the complete message to a JSON string
      String finalMessageJson = mapper.writeValueAsString(message);

      // Send the message using emit
      emit(finalMessageJson).block();
    } catch (Exception e) {
      System.err.println("Error serializing message: " + e.getMessage());
    }
  }
}
