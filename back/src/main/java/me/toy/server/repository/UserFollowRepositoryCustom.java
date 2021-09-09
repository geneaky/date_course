package me.toy.server.repository;

public interface UserFollowRepositoryCustom {

  public void deleteUserFollowInUserFollowings(Long userId, Long followerId);

}
