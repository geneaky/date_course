package me.toy.server.repository;

import static me.toy.server.entity.QUserFollow.userFollow;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import me.toy.server.entity.QUserFollow;
import me.toy.server.entity.UserFollow;

@RequiredArgsConstructor
public class UserFollowRepositoryImpl implements UserFollowRepositoryCustom {

  private final JPAQueryFactory queryFactory;


  @Override
  public boolean isFollow(Long userId, Long followerId) {

    UserFollow userFollow = queryFactory
        .selectFrom(QUserFollow.userFollow)
        .where(QUserFollow.userFollow.user.id.eq(userId)
            .and(QUserFollow.userFollow.follow.followUserId.eq(followerId)))
        .fetchOne();

    return userFollow != null;
  }

  @Override
  public void deleteUserFollow(Long userId, Long followerId) {

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
