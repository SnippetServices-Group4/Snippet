package com.services.group4.snippet.services.async;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.group4.snippet.common.Language;
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
public class FormatEventConsumer extends RedisStreamConsumer<String> {
  private final ObjectMapper mapper;
  private final SnippetService snippetService;
  private final FinalFormatEventProducer publisher;

  @Autowired
  public FormatEventConsumer(
      @Value("${stream.initial.format.key}") String streamKey,
      @Value("${groups.format}") String groupId,
      @NotNull RedisTemplate<String, String> redis,
      @NotNull SnippetService snippetService,
      @NotNull FinalFormatEventProducer publisher) {
    super(streamKey, groupId, redis);
    this.mapper = new ObjectMapper();
    this.snippetService = snippetService;
    this.publisher = publisher;
  }

  @Override
  protected void onMessage(@NotNull ObjectRecord<String, String> objectRecord) {
    System.out.println("\nFORMAT EVENT CONSUMER\n\n");

    String jsonString = objectRecord.getValue();

    try {
      Map<String, Object> messageMap = mapper.readValue(jsonString, new TypeReference<>() {});

      Long snippetId = (Long) ((Integer) messageMap.get("snippetId")).longValue();
      Language language = snippetService.getLanguage(snippetId);

      String langName = language.getLangName();
      String version = language.getVersion();

      messageMap.put("language", langName);
      messageMap.put("version", version);

      publisher.publishEvent(messageMap);
    } catch (Exception e) {
      System.err.println("Error deserializing message: " + e.getMessage());
    }
  }

  @Override
  protected @NotNull StreamReceiver.StreamReceiverOptions<String, ObjectRecord<String, String>>
      options() {
    return StreamReceiver.StreamReceiverOptions.builder()
        .pollTimeout(Duration.ofSeconds(3))
        .targetType(String.class)
        .build();
  }
}
