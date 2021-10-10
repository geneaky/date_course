package me.toy.server.repository;

import static me.toy.server.entity.QFollow.follow;
import static me.toy.server.entity.QUser.user;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import me.toy.server.entity.User;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<User> findFollowees(Long followerId) {
    return queryFactory
        .selectFrom(user)
        .where(
            user.id.in(
                JPAExpressions
                    .select(follow.followeeId)
                    .from(follow)
                    .where(follow.user.id.eq(followerId))
            )
        )
        .fetch();
  }

  @Override
  public List<User> findFollowers(Long followeeId) {
    return queryFactory
        .selectFrom(user)
        .where(
            user.id.in(
                JPAExpressions
                    .select(follow.user.id)
                    .from(follow)
                    .where(follow.followeeId.eq(followeeId))
            )
        )
        .fetch();
  }
}
