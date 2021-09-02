package me.toy.server.repository;

import me.toy.server.entity.DateCourse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DateCourseRepository extends JpaRepository<DateCourse, Long> {

  @EntityGraph(attributePaths = {"user"})
  Page<DateCourse> findAll(Pageable pageable);

  @EntityGraph(attributePaths = {"user"})
  @Query("select d from DateCourse d order by d.userDateCourseLikes.size desc")
  Page<DateCourse> findLikeOrderDateCourse(Pageable pageable);
//querydsl로 동적쿼리로 만들어서 역순 제공할 예정

  @EntityGraph(attributePaths = {"user"})
  Page<DateCourse> findAllDateCourseByUserId(@Param("userId") Long userId, Pageable pageable);

}
