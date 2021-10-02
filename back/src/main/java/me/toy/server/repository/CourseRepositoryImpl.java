package me.toy.server.repository;

import static me.toy.server.entity.QCourse.course;
import static me.toy.server.entity.QLocation.location;
import static me.toy.server.entity.QLocationTag.locationTag;
import static me.toy.server.entity.QTag.tag;
import static me.toy.server.entity.QUserCourseLike.userCourseLike;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPAExpressions;
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

    List<Course> courses = queryFactory
        .selectFrom(course)
        .leftJoin(course.locations, location).fetchJoin()
        .leftJoin(course.userCourseLikes, userCourseLike)
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    long total = queryFactory.selectFrom(course).fetchCount();

    if (direction.isAscending()) {

      List<CourseDto> results = getAscendingCourseDtos(courses);
      return new PageImpl<>(results, pageable, total);
    }
    List<CourseDto> results = getDescendingCourseDtos(courses);
    return new PageImpl<>(results, pageable, total);
  }

  @Override
  public Page<CourseDto> findCoursesByTag(String tagName, Pageable pageable) {

    QueryResults<Course> courseQueryResults = queryFactory
        .selectFrom(course)
        .where(
            course.id.in(
                JPAExpressions
                    .select(course.id)
                    .from(course)
                    .join(course.locations, location)
                    .join(location.locationTags, locationTag)
                    .join(locationTag.tag, tag)
                    .where(tag.name.eq(tagName))
            )
        )
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetchResults();

    List<CourseDto> courseDtos = courseQueryResults.getResults().stream()
        .map(CourseDto::new)
        .collect(Collectors.toList());

    return new PageImpl<>(courseDtos, pageable, courseQueryResults.getTotal());
  }

  @Override
  public Page<CourseDto> findCoursesByTitle(String title, Pageable pageable) {

    QueryResults<Course> courseQueryResults = queryFactory
        .selectFrom(course)
        .where(course.courseTitle.contains(title))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetchResults();

    List<CourseDto> courseDtos = courseQueryResults.getResults().stream()
        .map(CourseDto::new)
        .collect(Collectors.toList());

    return new PageImpl<>(courseDtos, pageable, courseQueryResults.getTotal());
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
