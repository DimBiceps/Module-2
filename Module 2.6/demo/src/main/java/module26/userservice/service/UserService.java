package module26.userservice.service;

import module26.userservice.web.dto.UserCreateUpdateDto;
import module26.userservice.web.dto.UserDto;

import java.util.List;

public interface UserService {
  Long create(UserCreateUpdateDto dto);

  UserDto get(Long id);

  List<UserDto> getAll();

  void update(Long id, UserCreateUpdateDto dto);

  void delete(Long id);
}
