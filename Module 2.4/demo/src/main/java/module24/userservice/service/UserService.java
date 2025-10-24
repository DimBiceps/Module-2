package module24.userservice.service;

import module24.userservice.web.dto.UserCreateUpdateDto;
import module24.userservice.web.dto.UserDto;

import java.util.List;

public interface UserService {
  Long create(UserCreateUpdateDto dto);
  UserDto get(Long id);
  List<UserDto> getAll();
  void update(Long id, UserCreateUpdateDto dto);
  void delete(Long id);
}
