package me.toy.server.repository;

import java.util.List;
import me.toy.server.entity.User;

public interface UserRepositoryCustom {

  public List<User> findAllFollowings(String userEmail);

  public List<User> findAllFollowers(Long followUserId);

}
