package me.toy.server.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;
import me.toy.server.entity.Comment;
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
        .course(new ArrayList<>())
        .userCourseLikes(new ArrayList<>())
        .userCourseSaves(new ArrayList<>())
        .comments(new ArrayList<>())
        .build();
  }

  @Test
  @DisplayName("데이트 코스에 댓글을 등록 시킨다")
  public void registCommentTest() throws Exception {

    String title = "testTitle";
    String userEmail = "test@naver.com";
    String comment = "test comment";
    User user = createUser();
    Course course = new Course(user, title);
    Comment dateCourseComment = new Comment(user, course, comment);

    when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
    when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));
    when(commentRepository.findById(dateCourseComment.getId()))
        .thenReturn(Optional.of(dateCourseComment));

    commentService.registComment(course.getId(), comment, userEmail);

    verify(commentRepository, times(1)).save(any());
    assertThat(commentRepository.findById(dateCourseComment.getId()).get().getContent())
        .isEqualTo(comment);
  }

  @Test
  @DisplayName("인가 받지 않은 사용자의 댓글 등록은 예외가 발생합니다.")
  public void registCommentWithUnAuthorizedUser() throws Exception {

    String unAuthorizedUserEmail = "NONONO@gmail.com";
    Long courseId = 1L;
    String comment = "test comment";

    assertThrows(UserNotFoundException.class, () -> {
      commentService.registComment(courseId, comment, unAuthorizedUserEmail);
    });
  }

  @Test
  @DisplayName("존재 하지 않는 코스에 댓글 등록시 예외가 발생합니다.")
  public void registCommentNotExistedCourseTest() throws Exception {

    User mockedUser = mock(User.class);
    String mockedUserEmail = "OKOKOK@gmail.com";
    Long courseId = 1L;
    String comment = "test comment";

    when(userRepository.findByEmail(any())).thenReturn(Optional.ofNullable(mockedUser));

    assertThrows(CourseNotFoundException.class, () -> {
      commentService.registComment(courseId, comment, mockedUserEmail);
    });
  }
}