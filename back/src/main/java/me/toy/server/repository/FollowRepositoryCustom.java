package me.toy.server.repository;

public interface FollowRepositoryCustom {

  public void deleteFollowInUserFollowings(Long userId, Long followUserId);
}
