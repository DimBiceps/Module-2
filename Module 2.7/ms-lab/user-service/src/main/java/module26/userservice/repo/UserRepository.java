package module26.userservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import module26.userservice.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
  boolean existsByEmail(String email);
}
