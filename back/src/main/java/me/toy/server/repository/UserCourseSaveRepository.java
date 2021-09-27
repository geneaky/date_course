package me.toy.server.repository;

import me.toy.server.entity.UserCourseSave;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface UserCourseSaveRepository extends JpaRepository<UserCourseSave, Long> {

  void deleteByUserIdAndCourseId(Long userId, Long dateCourseId);

  @EntityGraph(attributePaths = {"user", "dateCourse"})
  Page<UserCourseSave> findAllUserCourseSavePageByUserId(
      @Param("userId") Long userId, Pageable pageable);
}
