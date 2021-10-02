package me.toy.server.repository;

import static me.toy.server.entity.QCourse.course;
import static me.toy.server.entity.QLocation.location;
import static me.toy.server.entity.QUserCourseLike.userCourseLike;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Comparator;
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

    Direction direction = Objects.requireNonNull(pageable.getSort().getOrderFor("likes"))
        .getDirection();

    List<Course> fetch = queryFactory
        .selectFrom(course)
        .leftJoin(course.locations, location).fetchJoin()
        .leftJoin(course.userCourseLikes, userCourseLike)
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    long total = queryFactory.selectFrom(course).fetchCount();

    if (direction.isAscending()) {

      List<CourseDto> results = getAscendingCourseDtos(fetch);
      return new PageImpl<>(results, pageable, total);
    }
    List<CourseDto> results = getDescendingCourseDtos(fetch);
    return new PageImpl<>(results, pageable, total);
  }

  private List<CourseDto> getAscendingCourseDtos(
      List<Course> fetch) {

    return fetch
        .stream()
        .map(CourseDto::new)
        .sorted(toAscend())
        .collect(Collectors.toList());
  }

  private List<CourseDto> getDescendingCourseDtos(
      List<Course> fetch) {

    return fetch
        .stream()
        .map(CourseDto::new)
        .sorted(toDescend())
        .collect(Collectors.toList());
  }

  private Comparator<CourseDto> toAscend() {
    return (courseDto1, courseDto2) -> {
      if (courseDto1.getLikesCount() > courseDto2.getLikesCount()) {
        return 1;
      }
      if (courseDto1.getLikesCount() == courseDto2.getLikesCount()) {
        if (courseDto1.getId() > courseDto2.getId()) {
          return 1;
        }
        return -1;
      }
      return -1;
    };
  }

  private Comparator<CourseDto> toDescend() {
    return (courseDto1, courseDto2) -> {
      if (courseDto1.getLikesCount() > courseDto2.getLikesCount()) {
        return -1;
      }
      if (courseDto1.getLikesCount() == courseDto2.getLikesCount()) {
        if (courseDto1.getId() > courseDto2.getId()) {
          return 1;
        }
        return -1;
      }
      return 1;
    };
  }

}
