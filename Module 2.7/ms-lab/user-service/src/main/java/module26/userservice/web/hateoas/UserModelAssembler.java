package module26.userservice.web.hateoas;

import module26.userservice.web.UserController;
import module26.userservice.web.dto.UserDto;
import org.springframework.hateoas.*;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<UserDto, EntityModel<UserDto>> {

    @Override
    public EntityModel<UserDto> toModel(UserDto user) {
        UserDto userDto = user;
        EntityModel<UserDto> model = EntityModel.of(userDto);
        model.add(linkTo(methodOn(UserController.class).get(userDto.id())).withSelfRel());
        model.add(linkTo(methodOn(UserController.class).getAll()).withRel("users"));
        model.add(linkTo(methodOn(UserController.class).update(userDto.id(), null)).withRel("update"));
        model.add(linkTo(methodOn(UserController.class).delete(userDto.id())).withRel("delete"));
        return model;
    }
}
