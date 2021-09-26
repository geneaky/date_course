package me.toy.server.repository;

import me.toy.server.entity.UserDateCourseSave;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface UserDateCourseSaveRepository extends JpaRepository<UserDateCourseSave, Long> {

  void deleteByUserIdAndDateCourseId(Long userId, Long dateCourseId);

  @EntityGraph(attributePaths = {"user", "dateCourse"})
  Page<UserDateCourseSave> findAllUserDateCourseSavePageByUserId(
      @Param("userId") Long userId, Pageable pageable);
}
