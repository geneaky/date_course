package me.toy.server.repository;

import me.toy.server.entity.LikeCourse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeCourseRepository extends JpaRepository<LikeCourse,Long> {
}
