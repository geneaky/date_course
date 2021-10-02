package me.toy.server.repository;

import me.toy.server.dto.course.CourseResponseDto.CourseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CourseRepositoryCustom {

  Page<CourseDto> findLikeOrderCourse(Pageable pageable);

}
