package module24.userservice.web;

import jakarta.validation.Valid;
import module24.userservice.service.UserService;
import module24.userservice.web.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
  private final UserService service;
  public UserController(UserService service){ this.service = service; }

  @GetMapping public List<UserDto> getAll(){ return service.getAll(); }
  @GetMapping("/{id}") public UserDto get(@PathVariable Long id){ return service.get(id); }

  @PostMapping @ResponseStatus(HttpStatus.CREATED)
  public Long create(@RequestBody @Valid UserCreateUpdateDto dto){ return service.create(dto); }

  @PutMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
  public void update(@PathVariable Long id, @RequestBody @Valid UserCreateUpdateDto dto){ service.update(id, dto); }

  @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id){ service.delete(id); }
}
