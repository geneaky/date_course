package me.toy.server.repository;

import me.toy.server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

//    public Optional<User> findByUsername(String username);

    public Optional<User> findByEmail(String email);
}
