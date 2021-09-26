package me.toy.server.repository;

import static me.toy.server.entity.QUserDateCourseLike.userDateCourseLike;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserDateCourseLikeRepositoryImpl implements UserDateCourseLikeRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public void unlikeDateCourse(Long userId, Long dateCourseId) {

    queryFactory
        .delete(userDateCourseLike)
        .where(userDateCourseLike.user.id.eq(userId)
            .and(userDateCourseLike.dateCourse.id.eq(dateCourseId)))
        .execute();
  }
}
