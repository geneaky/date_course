package me.toy.server.repository;

public interface CommentRepositoryCustom {

  void deleteCommentByUsing(Long courseId, Long userId, Long commentId);

}
