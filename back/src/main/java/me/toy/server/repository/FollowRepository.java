package me.toy.server.repository;

import me.toy.server.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FollowRepository extends JpaRepository<Follow, Long> {

  @Modifying
  @Query("DELETE FROM Follow f WHERE f.id IN (SELECT uf.follow.id FROM UserFollow uf where uf.user.id = :userId) AND f.followUserId =:followUserId")
  public void deleteFollowInUserFollowings(@Param("userId") Long userId,
      @Param("followUserId") Long followUserId);
}
