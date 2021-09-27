package me.toy.server.repository;

public interface UserFollowRepositoryCustom {

  public boolean isFollow(Long userId, Long followerId);

  public void deleteUserFollow(Long userId, Long followerId);

}
