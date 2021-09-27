package me.toy.server.repository;

import java.util.Optional;
import me.toy.server.entity.UserCourseLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCourseLikeRepository extends JpaRepository<UserCourseLike, Long>,
    UserCourseLikeRepositoryCustom {

  Optional<UserCourseLike> findLikeByUserIdAndCourseId(Long userId, Long courseId);
}
