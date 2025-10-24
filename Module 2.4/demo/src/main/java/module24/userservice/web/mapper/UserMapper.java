package module24.userservice.web.mapper;

import module24.userservice.domain.User;
import module24.userservice.web.dto.*;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {
  public UserDto toDto(User u) {
    return new UserDto(u.getId(), u.getName(), u.getEmail(), u.getAge(), u.getCreatedAt());
  }
  public void updateEntity(User u, UserCreateUpdateDto dto) {
    u.setName(dto.name());
    u.setEmail(dto.email());
    u.setAge(dto.age());
  }
  public User toEntity(UserCreateUpdateDto dto) {
    User u = new User();
    updateEntity(u, dto);
    return u;
  }
}
