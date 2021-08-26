package me.toy.server.repository;

import me.toy.server.entity.UserDateCourseLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserDateCourseLikeRepository extends JpaRepository<UserDateCourseLike, Long> {

  @Modifying
  @Query(" delete from UserDateCourseLike udl where udl.user.id = :userId and udl.dateCourse.id = :dateCourseId")
  public void unlikeUserDateCourseLike(@Param("userId") Long userId,
      @Param("dateCourseId") Long dateCourseId);
}
