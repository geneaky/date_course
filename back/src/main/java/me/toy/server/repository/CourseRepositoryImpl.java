package me.toy.server.repository;

import static me.toy.server.entity.QCourse.course;
import static me.toy.server.entity.QLocation.location;
import static me.toy.server.entity.QUserCourseLike.userCourseLike;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import me.toy.server.dto.course.CourseResponseDto.CourseDto;
import me.toy.server.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

@RequiredArgsConstructor
public class CourseRepositoryImpl implements CourseRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public Page<CourseDto> findLikeOrderCourse(Pageable pageable) {

    Direction likesCount = Objects.requireNonNull(pageable.getSort().getOrderFor("likesCount"))
        .getDirection();
    List<Course> fetch = queryFactory
        .selectFrom(course)
        .leftJoin(course.locations, location).fetchJoin()
        .leftJoin(course.userCourseLikes, userCourseLike)
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();
    long total = queryFactory.selectFrom(course).fetchCount();

    if (likesCount.isAscending()) {
      List<CourseDto> results = getAscendingLikeOrderCourseDtos(fetch);

      return new PageImpl<>(results, pageable, total);
    }

    List<CourseDto> results = getDescendingLikeOrderCourseDtos(fetch);
    return new PageImpl<>(results, pageable, total);
  }

  private List<CourseDto> getDescendingLikeOrderCourseDtos(
      List<Course> fetch) {

    return fetch
        .stream()
        .map(CourseDto::new)
        .sorted((dateCourseDto1, dateCourseDto2) -> {
          if (dateCourseDto1.getLikesCount() > dateCourseDto2.getLikesCount()) {
            return -1;
          } else if (dateCourseDto1.getLikesCount() == dateCourseDto2.getLikesCount()) {
            if (dateCourseDto1.getId() > dateCourseDto2.getId()) {
              return 1;
            }
            return -1;
          }
          return 1;
        })
        .collect(Collectors.toList());
  }

  private List<CourseDto> getAscendingLikeOrderCourseDtos(
      List<Course> fetch) {

    return fetch
        .stream()
        .map(CourseDto::new)
        .sorted((dateCourseDto1, dateCourseDto2) -> {
          if (dateCourseDto1.getLikesCount() > dateCourseDto2.getLikesCount()) {
            return 1;
          } else if (dateCourseDto1.getLikesCount() == dateCourseDto2.getLikesCount()) {
            if (dateCourseDto1.getId() > dateCourseDto2.getId()) {
              return 1;
            }
            return -1;
          }
          return -1;
        })
        .collect(Collectors.toList());
  }

}
