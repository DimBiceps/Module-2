// src/main/java/module22/service/DefaultUserService.java
package module23.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import module22.dao.UserDao;
import module22.entity.User;

public class DefaultUserService implements UserService {
    private static final Logger log = LoggerFactory.getLogger(DefaultUserService.class);
    private final UserDao userDao;

    public DefaultUserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override public Long createUser(User user) {
        Long id = userDao.create(user);
        log.info("User created: id={}", id);
        return id;
    }
    @Override public List<User> getAllUsers() { return userDao.findAll(); }
    @Override public Optional<User> getUserById(Long id) { return userDao.findById(id); }
    @Override public void updateUser(User user) { userDao.update(user); log.info("User updated: id={}", user.getId()); }
    @Override public void deleteUser(Long id) {
        Optional<User> u = userDao.findById(id);
        if (u.isPresent()) { userDao.delete(u.get()); log.info("User deleted: id={}", id); }
        else { log.warn("User not found: id={}", id); }
    }
}
