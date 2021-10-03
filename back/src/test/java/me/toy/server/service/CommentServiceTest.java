package me.toy.server.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import me.toy.server.entity.Course;
import me.toy.server.entity.User;
import me.toy.server.exception.course.CourseNotFoundException;
import me.toy.server.exception.user.UserNotFoundException;
import me.toy.server.repository.CommentRepository;
import me.toy.server.repository.CourseRepository;
import me.toy.server.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

  @Mock
  CommentRepository commentRepository;
  @Mock
  UserRepository userRepository;
  @Mock
  CourseRepository courseRepository;
  @InjectMocks
  CommentService commentService;

  private User createUser() {

    return User.builder()
        .email("test@naver.com")
        .build();
  }

  @Test
  @DisplayName("데이트 코스에 댓글을 등록 시킨다")
  public void registCommentTest() throws Exception {

    String comment = "test comment";
    User user = createUser();
    Course course = new Course(user, "testTitle");

    when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
    when(courseRepository.findById(any())).thenReturn(Optional.of(course));

    commentService.registComment(course.getId(), comment, user.getEmail());

    verify(commentRepository).save(any());
  }

  @Test
  @DisplayName("인가 받지 않은 사용자의 댓글 등록은 예외가 발생합니다.")
  public void registCommentWithUnAuthorizedUser() throws Exception {

    assertThrows(UserNotFoundException.class, () -> {
      commentService.registComment(any(), "test", "NONO@naver.com");
    });
  }

  @Test
  @DisplayName("존재 하지 않는 코스에 댓글 등록시 예외가 발생합니다.")
  public void registCommentNotExistedCourseTest() throws Exception {

    User user = createUser();

    when(userRepository.findByEmail(any())).thenReturn(Optional.ofNullable(user));

    assertThrows(CourseNotFoundException.class, () -> {
      commentService.registComment(1L, "test comment", user.getEmail());
    });
  }
}