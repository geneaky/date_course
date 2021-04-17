package me.toy.server.repository;

import me.toy.server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface UserRepository extends JpaRepository<User,Long> {

    public User findByUsername(String username);

}
