package me.toy.server.repository;


import static me.toy.server.entity.QFollow.follow;
import static me.toy.server.entity.QUserFollow.userFollow;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FollowRepositoryImpl implements FollowRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public void deleteFollow(Long userId, Long followUserId) {

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
