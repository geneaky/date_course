package me.toy.server.repository;

import static me.toy.server.entity.QFollow.*;
import static me.toy.server.entity.QUser.*;
import static me.toy.server.entity.QUserFollow.*;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import me.toy.server.entity.User;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<User> findAllFollowingUsers(String userEmail) {

    return queryFactory
        .selectFrom(user)
        .where(user.id.in(
            JPAExpressions
                .select(follow.followUserId)
                .from(follow)
                .join(follow.userFollows, userFollow)
                .join(userFollow.user, user)
                .where(user.email.eq(userEmail)
                    .and(userFollow.follow.id.eq(follow.id))
                    .and(user.id.eq(userFollow.user.id)))
        )).fetch();
  }

  @Override
  public List<User> findAllFollowerUsers(Long followUserId) {

    return queryFactory
        .selectFrom(user)
        .where(user.id.in(
            JPAExpressions
                .select(userFollow.user.id)
                .from(userFollow)
                .join(userFollow.follow, follow)
                .where(userFollow.follow.id.eq(follow.id)
                    .and(follow.followUserId.eq(followUserId)))
        )).fetch();
  }
}
