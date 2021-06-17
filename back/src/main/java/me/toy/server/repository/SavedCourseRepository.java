package me.toy.server.repository;

import me.toy.server.entity.SavedCourse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavedCourseRepository extends JpaRepository<SavedCourse,Long> {
}
