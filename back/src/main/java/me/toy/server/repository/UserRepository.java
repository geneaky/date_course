package me.toy.server.repository;

import java.util.Optional;
import me.toy.server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

  Optional<User> findByEmail(String email);

  Optional<User> findByEmailAndPassword(String email, String password);
}
