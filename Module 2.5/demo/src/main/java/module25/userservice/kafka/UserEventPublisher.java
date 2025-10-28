package module25.userservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserEventPublisher {
  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ObjectMapper objectMapper;
  private final String topic;

  public UserEventPublisher(
      KafkaTemplate<String, String> kafkaTemplate,
      ObjectMapper objectMapper,
      @Value("${app.kafka.user-topic:user.events}") String topic) {
    this.kafkaTemplate = kafkaTemplate;
    this.objectMapper = objectMapper;
    this.topic = topic;
  }

  public void publish(UserEvent event) {
    try {
      String payload = objectMapper.writeValueAsString(event);
      kafkaTemplate.send(topic, event.userId() != null ? event.userId().toString() : null, payload);
    } catch (Exception e) {
      throw new RuntimeException("Failed to publish user event", e);
    }
  }
}
