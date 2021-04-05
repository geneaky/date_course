package me.toy.server.repository;

import me.toy.server.entity.DateCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DateCourseRepository extends JpaRepository <DateCourse,Long> {
}
