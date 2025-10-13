package module23.config;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class TestHibernateUtil {
    private static final SessionFactory SESSION_FACTORY = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            Configuration cfg = new Configuration().configure();

            // Позволяем тестам/окружению переопределять настройки через -D... или System.setProperty(...)
            overrideFromSystem(cfg, "hibernate.connection.url");
            overrideFromSystem(cfg, "hibernate.connection.username");
            overrideFromSystem(cfg, "hibernate.connection.password");
            overrideFromSystem(cfg, "hibernate.hbm2ddl.auto");
            overrideFromSystem(cfg, "show_sql");
            overrideFromSystem(cfg, "format_sql");

            return cfg.buildSessionFactory();
        } catch (HibernateException ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    private static void overrideFromSystem(Configuration cfg, String key) {
        String v = System.getProperty(key);
        if (v != null && !v.isBlank()) {
            cfg.setProperty(key, v);
        }
    }

    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}
