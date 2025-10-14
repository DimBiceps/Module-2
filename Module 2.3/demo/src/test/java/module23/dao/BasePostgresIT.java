package module23.dao;

import module23.config.HibernateTestUtil;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BasePostgresIT {

    protected static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine")
                    .withDatabaseName("testdb")
                    .withUsername("postgres")
                    .withPassword("postgres");

    protected SessionFactory sessionFactory;

    @BeforeAll
    void startContainerAndBuildSessionFactory() {
        postgres.start(); // старт контейнера
        sessionFactory = HibernateTestUtil.buildSessionFactory(
                postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword()
        );
    }

    @AfterEach
    void cleanup() {
        // изоляция тестов
        var session = sessionFactory.openSession();
        var tx = session.beginTransaction();
        session.createNativeQuery("TRUNCATE TABLE users RESTART IDENTITY CASCADE").executeUpdate();
        tx.commit();
        session.close();
    }
}
