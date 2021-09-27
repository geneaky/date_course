package me.toy.server.repository;

import me.toy.server.dto.course.CourseResponseDto.LikeOrderCourseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CourseRepositoryCustom {

  Page<LikeOrderCourseDto> findLikeOrderCourse(Pageable pageable);

}
