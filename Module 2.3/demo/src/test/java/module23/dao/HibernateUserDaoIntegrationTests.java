package module23.dao;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.Transaction;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import module22.config.HibernateUtil;
import module22.dao.HibernateUserDao;
import module22.entity.User;

@Testcontainers
@TestMethodOrder(MethodOrderer.DisplayName.class)
class HibernateUserDaoIntegrationTests {

    // Ленивая статическая ссылка на DAO, будем использовать ваш продовый класс
    private static HibernateUserDao dao;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("user_db")
            .withUsername("postgres")
            .withPassword("postgres");

    @BeforeAll
    static void setUpAll() {
        // Переопределяем настройки Hibernate через System properties (HibernateUtil их подхватит)
        System.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        System.setProperty("hibernate.connection.username", postgres.getUsername());
        System.setProperty("hibernate.connection.password", postgres.getPassword());
        // Чтобы схема создалась автоматически в тестах
        System.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        System.setProperty("show_sql", "false");
        System.setProperty("format_sql", "false");

        // ВАЖНО: после того, как всё выставили, впервые обращаемся к SessionFactory
        HibernateUtil.getSessionFactory();

        dao = new HibernateUserDao();
    }

    @BeforeEach
    void cleanDB() {
        // Изоляция тестов: чистим таблицу перед каждым тестом
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.createNativeQuery("TRUNCATE TABLE users RESTART IDENTITY").executeUpdate();
            tx.commit();
        }
    }

    @Test
    @DisplayName("1) create() и findById() — happy path")
    void testCreateAndFindById() {
        Long id = dao.create(new User("Alice", "alice@example.com", 25));
        assertNotNull(id);

        Optional<User> u = dao.findById(id);
        assertTrue(u.isPresent());
        assertEquals("Alice", u.get().getName());
        assertEquals("alice@example.com", u.get().getEmail());
        assertEquals(25, u.get().getAge());
        assertNotNull(u.get().getCreatedAt());
    }

    @Test
    @DisplayName("2) findAll() — возвращает список")
    void testFindAll() {
        dao.create(new User("A", "a@ex.com", 20));
        dao.create(new User("B", "b@ex.com", null));

        List<User> list = dao.findAll();
        assertEquals(2, list.size());
    }

    @Test
    @DisplayName("3) update() — меняет поля")
    void testUpdate() {
        Long id = dao.create(new User("X", "x@ex.com", 30));
        User toUpdate = dao.findById(id).orElseThrow();
        toUpdate.setName("X2");
        toUpdate.setAge(31);
        dao.update(toUpdate);

        User updated = dao.findById(id).orElseThrow();
        assertEquals("X2", updated.getName());
        assertEquals(31, updated.getAge());
    }

    @Test
    @DisplayName("4) delete() — удаляет запись")
    void testDelete() {
        Long id = dao.create(new User("Del", "d@ex.com", 10));
        User u = dao.findById(id).orElseThrow();
        dao.delete(u);

        assertTrue(dao.findById(id).isEmpty());
    }

    @Test
    @DisplayName("5) Валидация: пустое имя — IllegalArgumentException")
    void testCreateValidationName() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> dao.create(new User("", "e@ex.com", 10)));
        assertTrue(ex.getMessage().toLowerCase().contains("name"));
    }

    @Test
    @DisplayName("6) Валидация: возраст вне диапазона — IllegalArgumentException")
    void testCreateValidationAge() {
        assertThrows(IllegalArgumentException.class,
                () -> dao.create(new User("A", "a@ex.com", 999)));
    }
}
