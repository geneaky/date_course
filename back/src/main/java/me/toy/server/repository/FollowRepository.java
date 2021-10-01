package me.toy.server.repository;

import java.util.Optional;
import me.toy.server.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {

  Optional<Follow> findByUserIdAndFolloweeId(Long UserId, Long followeeId);

}
