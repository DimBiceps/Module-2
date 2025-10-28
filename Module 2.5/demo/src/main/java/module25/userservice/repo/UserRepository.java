package module25.userservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import module25.userservice.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
  boolean existsByEmail(String email);
}
