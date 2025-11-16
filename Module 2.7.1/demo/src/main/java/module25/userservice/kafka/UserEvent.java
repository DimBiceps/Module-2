package module25.userservice.kafka;

import java.time.OffsetDateTime;

public record UserEvent(
    String type,      // "USER_CREATED" | "USER_DELETED"
    Long userId,
    String email,
    OffsetDateTime occurredAt
) {}
