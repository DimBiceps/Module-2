package notification.notificationservice.kafka;

import java.time.OffsetDateTime;

public record UserEvent(String type, Long userId, String email, OffsetDateTime occurredAt) {}
