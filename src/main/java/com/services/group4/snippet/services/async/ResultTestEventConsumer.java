package com.services.group4.snippet.services.async;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.group4.snippet.common.states.test.TestState;
import com.services.group4.snippet.services.TestCaseService;
import org.austral.ingsis.redis.RedisStreamConsumer;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.StreamReceiver;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;

@Component
public class ResultTestEventConsumer extends RedisStreamConsumer<String> {
  private final ObjectMapper mapper;
  private final TestCaseService testCaseService;

  public ResultTestEventConsumer(
      @Value("${stream.result.test.key}") String streamKey,
      @Value("${groups.test}") String groupId,
      @NotNull RedisTemplate<String, String> redis,
      @NotNull ObjectMapper mapper,
      @NotNull TestCaseService testCaseService) {
    super(streamKey, groupId, redis);
    this.mapper = mapper;
    this.testCaseService = testCaseService;
  }

  @Override
  protected void onMessage(@NotNull ObjectRecord<String, String> objectRecord) {
    System.out.println("\nRESULT TEST EVENT CONSUMER\n\n");

    String jsonString = objectRecord.getValue();
    try {
      Map<String, Object> messageMap = mapper.readValue(jsonString, new TypeReference<>() {});

      Long testCaseId = (Long) ((Integer) messageMap.get("testCaseId")).longValue();
      TestState testState = TestState.valueOf((String) messageMap.get("status"));

      testCaseService.updateTestState(testCaseId, testState);
    } catch (Exception e) {
      System.err.println("Error deserializing message: " + e.getMessage());
    }
  }

  @Override
  protected @NotNull StreamReceiver.StreamReceiverOptions<String, ObjectRecord<String, String>> options() {
    return StreamReceiver.StreamReceiverOptions.builder()
        .pollTimeout(Duration.ofSeconds(7))
        .targetType(String.class)
        .build();
  }
}
