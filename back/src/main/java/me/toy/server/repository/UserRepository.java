package me.toy.server.repository;

import me.toy.server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

  Optional<User> findByEmail(String email);

  Optional<User> findByName(String name);

  Optional<User> findByEmailAndPassword(String email, String password);
}
