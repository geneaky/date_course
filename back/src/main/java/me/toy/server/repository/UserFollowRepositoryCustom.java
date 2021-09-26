package me.toy.server.repository;

public interface UserFollowRepositoryCustom {

  public void deleteUserFollow(Long userId, Long followerId);

}
