package me.toy.server.repository;

import me.toy.server.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CourseRepositoryCustom {

  Page<Course> findLikeOrderCourses(Pageable pageable);

  Page<Course> findCoursesByTag(String[] tagName, Pageable pageable);

  Page<Course> findCoursesByTitle(String title, Pageable pageable);

}
