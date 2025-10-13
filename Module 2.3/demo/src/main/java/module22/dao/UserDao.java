package module22.dao;

import java.util.List;
import java.util.Optional;

import module22.entity.User;

public interface UserDao {
    Long create(User user);
    Optional<User> findById(Long id);
    List<User> findAll();
    void update(User user);
    void delete(User user);
}
