package com.services.group4.snippet.services.async;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.group4.snippet.common.states.snippet.LintStatus;
import com.services.group4.snippet.services.SnippetService;
import java.time.Duration;
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
public class ResultLintEventConsumer extends RedisStreamConsumer<String> {
  private final ObjectMapper mapper;
  private final SnippetService snippetService;

  @Autowired
  public ResultLintEventConsumer(
      @Value("${stream.result.lint.key}") String streamKey,
      @Value("${groups.lint}") String groupId,
      @NotNull RedisTemplate<String, String> redis,
      @NotNull ObjectMapper mapper,
      @NotNull SnippetService snippetService) {
    super(streamKey, groupId, redis);
    this.mapper = mapper;
    this.snippetService = snippetService;
  }

  @Override
  protected void onMessage(@NotNull ObjectRecord<String, String> objectRecord) {
    System.out.println("\nRESULT LINT EVENT PRODUCER\n\n");

    String jsonString = objectRecord.getValue();

    try {
      // Deserialize the JSON string into a Map
      Map<String, Object> messageMap = mapper.readValue(jsonString, new TypeReference<>() {});

      // Access specific fields from the Map
      Long snippetId = (Long) ((Integer) messageMap.get("snippetId")).longValue();
      LintStatus status = LintStatus.valueOf(messageMap.get("status").toString());

      snippetService.updateLintStatus(snippetId, status);
    } catch (Exception e) {
      System.err.println("Error deserializing message: " + e.getMessage());
    }
  }

  @Override
  protected @NotNull StreamReceiver.StreamReceiverOptions<String, ObjectRecord<String, String>>
      options() {
    return StreamReceiver.StreamReceiverOptions.builder()
        .pollTimeout(Duration.ofSeconds(2))
        .targetType(String.class)
        .build();
  }
}
