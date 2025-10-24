package module24.userservice.web.dto;

import jakarta.validation.constraints.*;

public record UserCreateUpdateDto(
  @NotBlank @Size(max=100) String name,
  @NotBlank @Email @Size(max=250) String email,
  @PositiveOrZero @Max(120) Integer age
) 
{
  
}
