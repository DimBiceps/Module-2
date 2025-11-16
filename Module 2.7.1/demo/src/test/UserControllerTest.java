package module25.userservice.web;

import module25.userservice.service.UserService;
import module25.userservice.web.dto.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {
  @Autowired MockMvc mvc;
  @MockBean UserService service;

  @Test
  void getAll_ok() throws Exception {
    Mockito.when(service.getAll()).thenReturn(List.of(new UserDto(1L,"A","a@a",20, OffsetDateTime.now())));
    mvc.perform(get("/api/users"))
       .andExpect(status().isOk())
       .andExpect(jsonPath("$[0].name").value("A"));
  }
  @Test
  void create_ok() throws Exception {
    Mockito.when(service.create(Mockito.any())).thenReturn(42L);
    mvc.perform(post("/api/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{'name':'A','email':'a@a','age':20}"))
       .andExpect(status().isCreated())
       .andExpect(content().string("42"));
  }
  @Test
  void create_validation_fail() throws Exception {
    mvc.perform(post("/api/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"name\":\"\",\"email\":\"bad\",\"age\":-1}"))
       .andExpect(status().isBadRequest());
  }
}
