package me.toy.server.repository;

public interface FollowRepositoryCustom {

  public void deleteFollow(Long userId, Long followUserId);
}
