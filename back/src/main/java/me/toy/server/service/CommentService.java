package me.toy.server.service;

import lombok.RequiredArgsConstructor;
import me.toy.server.entity.Comment;
import me.toy.server.entity.Course;
import me.toy.server.entity.User;
import me.toy.server.exception.course.CourseNotFoundException;
import me.toy.server.exception.user.UserNotFoundException;
import me.toy.server.repository.CommentRepository;
import me.toy.server.repository.CourseRepository;
import me.toy.server.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final UserRepository userRepository;
  private final CourseRepository courseRepository;
  private final CommentRepository commentRepository;

  @Transactional
  public void registComment(Long courseId, String comment, String userEmail) {

    User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
        new UserNotFoundException("해당 이메일을 가진 사용자는 없습니다.")
    );
    Course course = courseRepository.findById(courseId).orElseThrow(() ->
        new CourseNotFoundException("찾으시는 데이트 코스는 없습니다."));

    Comment courseComment = new Comment(user, course, comment);
    commentRepository.save(courseComment);
  }

  @Transactional
  public void removeComment(Long courseId, Long userId, Long commentId) {

    commentRepository.deleteCommentByUsing(courseId, userId, commentId);
  }
}
