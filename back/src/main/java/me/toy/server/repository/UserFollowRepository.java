package me.toy.server.repository;

import me.toy.server.entity.UserFollow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserFollowRepository extends JpaRepository<UserFollow, Long> {

  @Modifying
  @Query("DELETE FROM UserFollow uf WHERE uf.id IN (SELECT uf2.id FROM UserFollow uf2 WHERE uf2.user.id = :userId AND uf2.follow.followUserId = :followerId)")
  void deleteUserFollowInUserFollowings(@Param("userId") Long userId,
      @Param("followerId") Long followerId);
}
