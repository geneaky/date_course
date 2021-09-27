package me.toy.server.repository;

import me.toy.server.entity.DateCourse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface DateCourseRepository extends JpaRepository<DateCourse, Long>,
    DateCourseRepositoryCustom {

  @EntityGraph(attributePaths = {"user"})
  Page<DateCourse> findAll(Pageable pageable);

  @EntityGraph(attributePaths = {"user"})
  Page<DateCourse> findAllDateCourseByUserId(@Param("userId") Long userId, Pageable pageable);

}
