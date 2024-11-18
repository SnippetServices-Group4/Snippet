package com.services.group4.snippet.services.async;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.Arrays;
import java.util.Map;
import org.austral.ingsis.redis.RedisStreamConsumer;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.StreamReceiver;
import org.springframework.stereotype.Component;

@Component
public class MockTestEventConsumer extends RedisStreamConsumer<String> {
  @Autowired
  public MockTestEventConsumer(
      @Value("${stream.test.key}") String streamKey,
      @Value("mock-test-group") String groupId,
      @NotNull RedisTemplate<String, String> redis) {
    super(streamKey, groupId, redis);
  }

  @Override
  protected void onMessage(@NotNull ObjectRecord<String, String> objectRecord) {
    String jsonString = objectRecord.getValue();
    System.out.println("Received JSON: " + jsonString);

    ObjectMapper mapper = new ObjectMapper();
    try {
      // Deserialize the JSON string into a Map
      Map<String, Object> messageMap = mapper.readValue(jsonString, new TypeReference<>() {});
      System.out.println("Parsed JSON as Map: " + messageMap);

      // Access specific fields from the Map
      Long snippetId = (Long) ((Integer) messageMap.get("snippetId")).longValue();
      String inputsJson = (String) messageMap.get("inputs");
      String outputsJson = (String) messageMap.get("outputs");

      System.out.println("SnippetId: " + snippetId);
      System.out.println("Inputs JSON String: " + inputsJson);
      System.out.println("Outputs JSON String: " + outputsJson);

      // Optionally parse the `inputs` and `outputs` fields if needed
      String[] inputs = mapper.readValue(inputsJson, String[].class);
      String[] outputs = mapper.readValue(outputsJson, String[].class);

      System.out.println("Parsed Inputs as Array: " + Arrays.toString(inputs));
      System.out.println("Parsed Outputs as Array: " + Arrays.toString(outputs));
    } catch (Exception e) {
      System.err.println("Error deserializing message: " + e.getMessage());
    }
  }

  @Override
  protected @NotNull StreamReceiver.StreamReceiverOptions<String, ObjectRecord<String, String>>
      options() {
    return StreamReceiver.StreamReceiverOptions.builder()
        .pollTimeout(Duration.ofSeconds(1))
        .targetType(String.class)
        .build();
  }
}
