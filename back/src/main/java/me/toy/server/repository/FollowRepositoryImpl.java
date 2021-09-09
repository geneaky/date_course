package me.toy.server.repository;


import static me.toy.server.entity.QFollow.*;
import static me.toy.server.entity.QUserFollow.*;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import me.toy.server.entity.QFollow;
import me.toy.server.entity.QUserFollow;

@RequiredArgsConstructor
public class FollowRepositoryImpl implements FollowRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public void deleteFollowInUserFollowings(Long userId, Long followUserId) {

    queryFactory
        .delete(follow)
        .where(follow.followUserId.in(
            JPAExpressions
                .select(userFollow.follow.id)
                .from(userFollow)
                .where(userFollow.user.id.eq(userId)))
            .and(follow.followUserId.eq(followUserId))
        )
        .execute();
  }
}
