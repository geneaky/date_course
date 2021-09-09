package me.toy.server.repository;

import static me.toy.server.entity.QUserFollow.*;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import me.toy.server.entity.QUserFollow;

@RequiredArgsConstructor
public class UserFollowRepositoryImpl implements UserFollowRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public void deleteUserFollowInUserFollowings(Long userId, Long followerId) {

    QUserFollow userFollowSub = new QUserFollow("userFollowSub");

    queryFactory
        .delete(userFollow)
        .where(userFollow.id.in(
            JPAExpressions
                .select(userFollowSub.id)
                .from(userFollowSub)
                .where(userFollowSub.user.id.eq(userId)
                    .and(userFollowSub.follow.followUserId.eq(followerId)))
        ))
        .execute();
  }
}
