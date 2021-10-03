package me.toy.server.repository;

import java.util.Optional;
import me.toy.server.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface CourseRepository extends JpaRepository<Course, Long>,
    CourseRepositoryCustom {

  @EntityGraph(attributePaths = {"user"})
  Page<Course> findAll(Pageable pageable);

  @EntityGraph(attributePaths = {"user"})
  Optional<Course> findByIdAndUserId(Long id, Long userId);

  @EntityGraph(attributePaths = {"user"})
  Page<Course> findAllCourseByUserId(@Param("userId") Long userId, Pageable pageable);

}
