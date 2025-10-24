package module24.userservice.web.dto;

import java.time.OffsetDateTime;

public record UserDto(Long id, String name, String email, Integer age, OffsetDateTime createdAt) 
{
    
}
