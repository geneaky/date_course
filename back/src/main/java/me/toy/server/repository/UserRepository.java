package me.toy.server.repository;

import java.util.Optional;
import me.toy.server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

  @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
  Optional<User> findByEmail(String email);

  Optional<User> findByEmailAndPassword(String email, String password);
}
