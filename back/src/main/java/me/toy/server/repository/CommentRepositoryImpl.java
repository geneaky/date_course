package me.toy.server.repository;

import static me.toy.server.entity.QComment.comment;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public void deleteCommentByUsing(Long courseId, Long userId, Long commentId) {

    queryFactory
        .delete(comment)
        .where(
            comment.id.eq(commentId)
                .and(comment.user.id.eq(userId))
                .and(comment.course.id.eq(courseId)))
        .execute();
  }
}
