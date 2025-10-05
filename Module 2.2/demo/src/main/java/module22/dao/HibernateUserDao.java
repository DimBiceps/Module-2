package module22.dao;

import java.util.List;
import java.util.Optional;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.JDBCConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import module22.config.HibernateUtil;
import module22.entity.User;

public class HibernateUserDao implements UserDao {
    private static final Logger log = LoggerFactory.getLogger(HibernateUserDao.class);

    @Override
    public Long create(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.warn("Validation failed: name is empty");
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.warn("Validation failed: email is empty");
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (user.getAge() != null && (user.getAge() < 0 || user.getAge() > 120)) {
            log.warn("Validation failed: age {} out of range", user.getAge());
            throw new IllegalArgumentException("Age must be between 0 and 120");
        }

        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Long id = (Long) session.save(user);
            tx.commit();
            log.info("User created successfully: id={}, name={}, email={}, age={}", id, user.getName(), user.getEmail(), user.getAge());
            return id;
        } catch (JDBCConnectionException e) {
            if (tx != null) tx.rollback();
            log.error("Database connection error during create", e);
            throw e;
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            log.error("Hibernate error on create", e);
            throw e;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            log.error("Unexpected error on create", e);
            throw e;
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Optional user = Optional.ofNullable(session.get(User.class, id));
            if (user.isPresent()) {
                log.info("Find by id: found id={}", id);
            } else {
                log.warn("Find by id: not found id={}", id);
            }
            return Optional.ofNullable(session.get(User.class, id));
        }
    }

    @Override
    public List<User> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<User> users = session.createQuery("from User", User.class).getResultList();
            log.info("Find all: returned {}", users.size());
            return session.createQuery("from User", User.class).getResultList();
        }
    }

    @Override
    public void update(User user) {
        if (user.getName() != null && user.getName().isBlank()) {
            log.warn("Validation failed: name is empty");
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (user.getEmail() != null && user.getEmail().isBlank()) {
            log.warn("Validation failed: email is empty");
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (user.getAge() != null && (user.getAge() < 0 || user.getAge() > 120)) {
            log.warn("Validation failed: age {} out of range", user.getAge());
            throw new IllegalArgumentException("Age must be between 0 and 120");
        }

        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.update(user);
            tx.commit();
            log.info("User updated successfully: name={}, id={}, email={}", user.getName(), user.getId(), user.getEmail());
        } catch (JDBCConnectionException e) {
            if (tx != null) tx.rollback();
            log.error("Database connection error during update", e);
            throw e;
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            log.error("Hibernate error on update", e);
            throw e;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            log.error("Unexpected error on update", e);
            throw e;
        }
    }

    @Override
    public void delete(User user) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.delete(user);
            tx.commit();
            log.info("User deleted successfully: id={}, name={}, email={}, age={}, created_at={}", user.getId(), user.getName(), user.getEmail(), user.getAge(), user.getCreatedAt());
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            log.warn("Unexpected error on delete", e);
            throw e;
        }
    }
}
