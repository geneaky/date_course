package me.toy.server.repository;

import static me.toy.server.entity.QDateCourse.*;
import static me.toy.server.entity.QLocation.*;
import static me.toy.server.entity.QUserDateCourseLike.*;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import me.toy.server.dto.DateCourseResponseDto.LikeOrderDateCourseDto;
import me.toy.server.entity.DateCourse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

@RequiredArgsConstructor
public class DateCourseRepositoryImpl implements DateCourseRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public Page<LikeOrderDateCourseDto> findLikeOrderDateCourse(Pageable pageable) {

    Direction likesCount = Objects.requireNonNull(pageable.getSort().getOrderFor("likesCount"))
        .getDirection();
    List<DateCourse> fetch = queryFactory
        .selectFrom(dateCourse)
        .leftJoin(dateCourse.locations, location).fetchJoin()
        .leftJoin(dateCourse.userDateCourseLikes, userDateCourseLike)
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();
    long total = queryFactory.selectFrom(dateCourse).fetchCount();

    if (likesCount.isAscending()) {
      List<LikeOrderDateCourseDto> results = getAscendingLikeOrderDateCourseDtos(fetch);

      return new PageImpl<>(results, pageable, total);
    }

    List<LikeOrderDateCourseDto> results = getDescendingLikeOrderDateCourseDtos(fetch);
    return new PageImpl<>(results, pageable, total);
  }

  private List<LikeOrderDateCourseDto> getDescendingLikeOrderDateCourseDtos(
      List<DateCourse> fetch) {

    return fetch
        .stream()
        .map(d -> new LikeOrderDateCourseDto(d.getId(), d.getUserDateCourseLikes().size(),
            d.getLocations()))
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

  private List<LikeOrderDateCourseDto> getAscendingLikeOrderDateCourseDtos(
      List<DateCourse> fetch) {

    return fetch
        .stream()
        .map(d -> new LikeOrderDateCourseDto(d.getId(), d.getUserDateCourseLikes().size(),
            d.getLocations()))
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
