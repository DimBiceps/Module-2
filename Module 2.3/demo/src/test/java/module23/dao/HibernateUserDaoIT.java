package module23.dao;

import module22.dao.HibernateUserDao;
import module22.entity.User;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class HibernateUserDaoIT extends BasePostgresIT {

    @Test
    void create_and_findById_work() {
        var dao = new HibernateUserDao(sessionFactory);
        
        Long id = dao.create(new User("Alice", "alice@example.com", 25));
        assertNotNull(id);

        Optional<User> loaded = dao.findById(id);
        assertTrue(loaded.isPresent());
        assertEquals("Alice", loaded.get().getName());
    }

    @Test
    void findAll_returns_inserted_rows() {
        var dao = new HibernateUserDao(sessionFactory);
        dao.create(new User("A", "a@a", 20));
        dao.create(new User("B", "b@b", 21));

        List<User> users = dao.findAll();
        assertEquals(2, users.size());
    }

    @Test
    void update_updates_fields() {
        var dao = new HibernateUserDao(sessionFactory);
        Long id = dao.create(new User("Bob", "bob@example.com", 31));

        var u = dao.findById(id).orElseThrow();
        u.setName("Robert");
        u.setAge(32);
        dao.update(u);

        var check = dao.findById(id).orElseThrow();
        assertEquals("Robert", check.getName());
        assertEquals(32, check.getAge());
    }

    @Test
    void delete_removes_row() {
        var dao = new HibernateUserDao(sessionFactory);
        Long id = dao.create(new User("Kek", "k@k", 40));

        var u = dao.findById(id).orElseThrow();
        dao.delete(u);

        assertTrue(dao.findById(id).isEmpty());
    }

    @Test
    void create_validates_fields() {
        var dao = new HibernateUserDao(sessionFactory);
        assertThrows(IllegalArgumentException.class, () -> dao.create(new User("", "e@e", 22)));
        assertThrows(IllegalArgumentException.class, () -> dao.create(new User("A", "", 22)));
        assertThrows(IllegalArgumentException.class, () -> dao.create(new User("A", "e@e", 999)));
    }
}
