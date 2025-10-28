package module25.userservice.service;

import module25.userservice.kafka.UserEventPublisher;
import module25.userservice.kafka.UserEvent;
import java.time.OffsetDateTime;

import module25.userservice.domain.User;
import module25.userservice.repo.UserRepository;
import module25.userservice.web.dto.*;
import module25.userservice.web.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DefaultUserService implements UserService {
  private final UserEventPublisher publisher;

  private final UserRepository repo;
  private final UserMapper mapper;
  public DefaultUserService(UserRepository repo, UserMapper mapper, UserEventPublisher publisher) {
    this.repo = repo; this.mapper = mapper; this.publisher = publisher;
  }

  @Override
  public Long create(UserCreateUpdateDto dto) {
    if (repo.existsByEmail(dto.email()))
      throw new IllegalArgumentException("Email already exists");
    User saved = repo.save(mapper.toEntity(dto));
    publisher.publish(new UserEvent("USER_CREATED", saved.getId(), saved.getEmail(), OffsetDateTime.now()));
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
    User u = repo.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
    repo.deleteById(id);
    publisher.publish(new UserEvent("USER_DELETED", id, u.getEmail(), OffsetDateTime.now()));
  }
}
