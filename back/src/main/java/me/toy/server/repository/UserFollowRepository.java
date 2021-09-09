package me.toy.server.repository;

import me.toy.server.entity.UserFollow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFollowRepository extends JpaRepository<UserFollow, Long>,
    UserFollowRepositoryCustom {

}
