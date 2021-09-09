package me.toy.server.repository;

import me.toy.server.dto.DateCourseResponseDto.LikeOrderDateCourseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DateCourseRepositoryCustom {

  Page<LikeOrderDateCourseDto> findLikeOrderDateCourse(Pageable pageable);

}
