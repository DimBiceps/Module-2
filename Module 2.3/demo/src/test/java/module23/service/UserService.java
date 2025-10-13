// src/main/java/module22/service/UserService.java
package module23.service;

import java.util.List;
import java.util.Optional;

import module22.entity.User;

public interface UserService {
    Long createUser(User user);
    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
    void updateUser(User user);
    void deleteUser(Long id);
}
