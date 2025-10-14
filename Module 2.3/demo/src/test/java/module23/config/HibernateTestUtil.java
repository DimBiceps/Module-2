package module23.config;

import module22.entity.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.util.Properties;

public final class HibernateTestUtil {
    private HibernateTestUtil() {}

    public static SessionFactory buildSessionFactory(String jdbcUrl, String username, String password) {
        Properties props = new Properties();
        props.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        props.setProperty("hibernate.connection.url", jdbcUrl);
        props.setProperty("hibernate.connection.username", username);
        props.setProperty("hibernate.connection.password", password);
        props.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");

        // настройки для интеграционных тестов 
        props.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        props.setProperty("hibernate.show_sql", "false");
        props.setProperty("hibernate.format_sql", "false");
        props.setProperty("hibernate.connection.pool_size", "3");

        Configuration cfg = new Configuration();
        cfg.addProperties(props);
        cfg.addAnnotatedClass(User.class);

        return cfg.buildSessionFactory();
    }
}
