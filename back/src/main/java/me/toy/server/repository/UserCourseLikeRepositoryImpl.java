package me.toy.server.repository;

import static me.toy.server.entity.QUserCourseLike.userCourseLike;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserCourseLikeRepositoryImpl implements UserCourseLikeRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public void unlikeCourse(Long userId, Long courseId) {

    queryFactory
        .delete(userCourseLike)
        .where(userCourseLike.user.id.eq(userId)
            .and(userCourseLike.course.id.eq(courseId)))
        .execute();
  }
}
