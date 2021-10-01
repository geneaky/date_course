package me.toy.server.repository;

import java.util.List;
import me.toy.server.entity.User;

public interface UserRepositoryCustom {

  List<User> findFollowees(Long followerId);

  List<User> findFollowers(Long followeeId);

}
