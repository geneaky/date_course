package me.toy.server.repository;

import java.util.Optional;
import me.toy.server.entity.UserDateCourseLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDateCourseLikeRepository extends JpaRepository<UserDateCourseLike, Long>,
    UserDateCourseLikeRepositoryCustom {

  Optional<UserDateCourseLike> findLikeByUserIdAndDateCourseId(Long userId, Long dateCourseId);
}
