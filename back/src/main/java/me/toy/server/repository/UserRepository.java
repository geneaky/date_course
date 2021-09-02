package me.toy.server.repository;

import java.util.List;
import me.toy.server.dto.UserResponseDto;
import me.toy.server.dto.UserResponseDto.UserFollowingUsers;
import me.toy.server.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

  Optional<User> findByName(String name);

  @EntityGraph(attributePaths = {"userFollows"})
  @Query(value = "SELECT re FROM User re WHERE re.id IN"
      + "(SELECT f.followUserId FROM Follow f JOIN f.userFollows fu ON fu.follow.id = f.id JOIN fu.user u ON u.id = fu.user.id WHERE u.email = :userEmail)")
  List<User> findAllFollowingUsers(@Param("userEmail") String userEmail);
}
