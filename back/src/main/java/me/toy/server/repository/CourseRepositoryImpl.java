package me.toy.server.repository;

import static me.toy.server.entity.QCourse.course;
import static me.toy.server.entity.QLocation.location;
import static me.toy.server.entity.QLocationTag.locationTag;
import static me.toy.server.entity.QTag.tag;
import static me.toy.server.entity.QUser.user;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import me.toy.server.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class CourseRepositoryImpl implements CourseRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public Page<Course> findLikeOrderCourses(Pageable pageable) {
    List<Course> courses = queryFactory
        .selectFrom(course)
        .join(course.user, user).fetchJoin()
        .join(course.locations, location).fetchJoin()
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();
    long total = queryFactory.selectFrom(course).fetchCount();

    return new PageImpl<>(courses, pageable, total);
  }

  @Override
  public Page<Course> findCoursesByTag(String[] tagNames, Pageable pageable) {
    QueryResults<Course> courseQueryResults = queryFactory
        .selectFrom(course)
        .join(course.locations, location).fetchJoin()
        .where(
            location.id.in(
                JPAExpressions
                    .select(location.id)
                    .from(location)
                    .join(location.locationTags, locationTag)
                    .join(locationTag.tag, tag)
                    .where(tag.name.in(tagNames))
                    .groupBy(location.id)
                    .having(tag.count().goe((long) tagNames.length))
            )
        )
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetchResults();

    return new PageImpl<>(courseQueryResults.getResults(), pageable, courseQueryResults.getTotal());
  }

  @Override
  public Page<Course> findCoursesByTitle(String title, Pageable pageable) {
    QueryResults<Course> courseQueryResults = queryFactory
        .selectFrom(course)
        .where(course.courseTitle.contains(title))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetchResults();

    return new PageImpl<>(courseQueryResults.getResults(), pageable, courseQueryResults.getTotal());
  }
}
