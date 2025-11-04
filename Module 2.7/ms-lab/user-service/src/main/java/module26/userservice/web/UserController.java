package module26.userservice.web;

import jakarta.validation.Valid;
import module26.userservice.service.UserService;
import module26.userservice.web.dto.*;
import module26.userservice.web.hateoas.UserModelAssembler;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Tag(name = "Users", description = "Операции над пользователями")
@RestController
@RequestMapping("/api/users")
public class UserController {
  private final UserService service;
  private final UserModelAssembler assembler;

  public UserController(UserService service, UserModelAssembler assembler) {
    this.service = service;
    this.assembler = assembler;
  }

  @Operation(summary = "Получить всех пользователей")
  @GetMapping
  public CollectionModel<EntityModel<UserDto>> getAll() {
    var models = service.getAll().stream()
        .map(assembler::toModel)
        .toList();
    return CollectionModel.of(models,
        linkTo(methodOn(UserController.class).getAll()).withSelfRel());
  }

  @Operation(summary = "Получить пользователя по ID")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Найдено"),
      @ApiResponse(responseCode = "404", description = "Пользователь не найден")
  })
  @GetMapping("/{id}")
  public EntityModel<UserDto> get(@PathVariable Long id) {
    return assembler.toModel(service.get(id));
  }

  @Operation(summary = "Создать пользователя")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Создано"),
      @ApiResponse(responseCode = "400", description = "Валидация не пройдена")
  })
  @PostMapping
  public ResponseEntity<EntityModel<UserDto>> create(@RequestBody @Valid UserCreateUpdateDto dto) {
    Long id = service.create(dto);
    var model = assembler.toModel(service.get(id));
    return ResponseEntity
        .created(linkTo(methodOn(UserController.class).get(id)).toUri())
        .body(model);
  }

  @Operation(summary = "Обновить пользователя")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Обновлено"),
      @ApiResponse(responseCode = "404", description = "Пользователь не найден")
  })
  @PutMapping("/{id}")
  public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody @Valid UserCreateUpdateDto dto) {
    service.update(id, dto);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Удалить пользователя")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Удалено"),
      @ApiResponse(responseCode = "404", description = "Пользователь не найден")
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
