package module23.service;

import module22.dao.UserDao;
import module22.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class DefaultUserService implements UserService {
    private static final Logger log = LoggerFactory.getLogger(DefaultUserService.class);
    private final UserDao userDao;

    public DefaultUserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override public Long createUser(User user) {
        Long id = userDao.create(user);
        log.info("User created: id={}, email={}", id, user.getEmail());
        return id;
    }

    @Override public List<User> getAllUsers() {
        List<User> list = userDao.findAll();
        log.info("Fetched users: {}", list.size());
        return list;
    }

    @Override public Optional<User> getUserById(Long id) {
        Optional<User> u = userDao.findById(id);
        if (u.isPresent()) log.info("User loaded: id={}", id);
        else log.warn("User not found: id={}", id);
        return u;
    }

    @Override public void updateUser(User user) {
        userDao.update(user);
        log.info("User updated: id={}", user.getId());
    }

    @Override public void deleteUser(Long id) {
        Optional<User> u = userDao.findById(id);
        if (u.isEmpty()) {
            log.warn("Delete skipped, not found: id={}", id);
            return;
        }
        userDao.delete(u.get());
        log.info("User deleted: id={}", id);
    }
}
