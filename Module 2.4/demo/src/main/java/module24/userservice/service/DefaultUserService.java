package module24.userservice.service;

import module24.userservice.domain.User;
import module24.userservice.repo.UserRepository;
import module24.userservice.web.dto.*;
import module24.userservice.web.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DefaultUserService implements UserService {
  private final UserRepository repo;
  private final UserMapper mapper;
  public DefaultUserService(UserRepository repo, UserMapper mapper) {
    this.repo = repo; this.mapper = mapper;
  }

  @Override
  public Long create(UserCreateUpdateDto dto) {
    if (repo.existsByEmail(dto.email()))
      throw new IllegalArgumentException("Email already exists");
    User saved = repo.save(mapper.toEntity(dto));
    return saved.getId();
  }

  @Override @Transactional(readOnly = true)
  public UserDto get(Long id) {
    User u = repo.findById(id).orElseThrow(() -> {
        return new NotFoundException("User not found");
    });
    return mapper.toDto(u);
  }

  @Override @Transactional(readOnly = true)
  public List<UserDto> getAll() {
    return repo.findAll().stream().map(mapper::toDto).toList();
  }

  @Override
  public void update(Long id, UserCreateUpdateDto dto) {
    User u = repo.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
    mapper.updateEntity(u, dto);
    repo.save(u);
  }

  @Override
  public void delete(Long id) {
    if (!repo.existsById(id)) throw new NotFoundException("User not found");
    repo.deleteById(id);
  }
}
