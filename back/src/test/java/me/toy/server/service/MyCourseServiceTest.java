package me.toy.server.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import me.toy.server.dto.course.CourseResponseDto.CourseDto;
import me.toy.server.dto.user.UserResponseDto.SavedCourseDto;
import me.toy.server.entity.Course;
import me.toy.server.entity.User;
import me.toy.server.entity.UserCourseLike;
import me.toy.server.entity.UserCourseSave;
import me.toy.server.exception.course.CourseNotFoundException;
import me.toy.server.exception.user.UserNotFoundException;
import me.toy.server.repository.CourseRepository;
import me.toy.server.repository.UserCourseSaveRepository;
import me.toy.server.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class MyCourseServiceTest {

  @Mock
  UserRepository userRepository;
  @Mock
  CourseRepository courseRepository;
  @Mock
  UserCourseSaveRepository userCourseSaveRepository;
  @InjectMocks
  MyCourseService myCourseService;

  private User createUser() {

    return User.builder()
        .id(1L)
        .email("test@naver.com")
        .build();
  }

  @Test
  @DisplayName("사용자가 좋아요 누른 코스의 ID 목록을 반환한다")
  public void getUserLikedDateCourseIDs() throws Exception {

    User user = createUser();
    Course course = new Course(user, "test");
    UserCourseLike userCourseLike1 = new UserCourseLike(user, course);
    UserCourseLike userCourseLike2 = new UserCourseLike(user, course);
    UserCourseLike userCourseLike3 = new UserCourseLike(user, course);

    when(userRepository.findByEmail(any()))
        .thenReturn(Optional.of(user));

    List<Long> likedCourse = myCourseService.getLikedCourseIds(any());

    assertThat(likedCourse).size().isEqualTo(3);
  }

  @Test
  @DisplayName("인가 받지 않은 사용자가 좋아요를 등록한 코스의 ID를 요청시 예외가 발생한다.")
  public void getLikedCourseIdsWithUnAuthorizedUser() throws Exception {

    when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> {
      myCourseService.getLikedCourseIds(any());
    });
  }

  @Test
  @DisplayName("사용자가 좋아요 누른 코스가 없을시 빈 목록을 반환한다")
  public void whenNoUserLikedDateCourseThenReturnEmptyList() throws Exception {

    User user = createUser();

    when(userRepository.findByEmail(any()))
        .thenReturn(Optional.of(user));

    List<Long> likedCourse = myCourseService.getLikedCourseIds(any());

    assertThat(likedCourse).isEmpty();
  }

  @Test
  @DisplayName("사용자가 저장할 코스를 등록한다.")
  public void registDateCourseUserWantToSave() throws Exception {

    User user = createUser();
    Course course = new Course(user, "test");

    when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
    when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));

    myCourseService.addCourse(course.getId(), user.getEmail());

    verify(userCourseSaveRepository).save(any());
  }

  @Test
  @DisplayName("인가 받지 않은 사용자가 코스 등록시 예외가 발생한다.")
  public void addCourseWithUnAuthorizedUserTest() throws Exception {

    User user = createUser();

    when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> {
      myCourseService.addCourse(any(), user.getEmail());
    });
  }

  @Test
  @DisplayName("존재하지 않는 코스를 등록하려고 하면 예외가 발생한다.")
  public void addNotExistedCourseTest() throws Exception {

    User user = createUser();

    when(userRepository.findByEmail(any())).thenReturn(Optional.ofNullable(user));
    when(courseRepository.findById(any())).thenReturn(Optional.empty());

    assertThrows(CourseNotFoundException.class, () -> {
      myCourseService.addCourse(1L, user.getEmail());
    });
  }


  @Test
  @DisplayName("사용자가 저장한 데이트 코스의 ID 목록을 반환한다")
  public void getUserSavedDateCourseIDs() throws Exception {

    User user = createUser();
    Course course = new Course(user, "test");
    UserCourseSave userCourseSave = new UserCourseSave(user, course);

    when(userRepository.findByEmail("test@naver.com")).thenReturn(Optional.of(user));

    List<Long> resultSavedCourse = myCourseService.getSavedCourseIds(user.getEmail());

    assertThat(resultSavedCourse).size().isEqualTo(1);
  }

  @Test
  @DisplayName("인가 받지 않은 사용자가 저장된 코스 ID 요청시 예외가 발생한다.")
  public void getSavedCourseIdsWithUnAuthorizedUserTest() throws Exception {

    when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> {
      myCourseService.getSavedCourseIds("NONO@naver.com");
    });
  }

  @Test
  @DisplayName("사용자가 저장한 데이트 코스가 없을시 빈 목록을 반환한다")
  public void whenNoUserSavedDateCourseThenReturnEmptyList() throws Exception {

    User user = createUser();

    when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
    List<Long> savedCourse = myCourseService.getSavedCourseIds(user.getEmail());

    assertThat(savedCourse).isEmpty();
  }

  @Test
  @DisplayName("사용자가 지정한 데이트 코스를 저장한 데이트 코스에서 삭제한다")
  public void deleteDateCourseAtUserSavedDateCourses() throws Exception {

    User user = createUser();
    Course course = new Course(user, "test");

    when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));

    myCourseService.removeCourse(course.getId(), user.getId());

    verify(userCourseSaveRepository).deleteByUserIdAndCourseId(any(), any());
  }

  @Test
  @DisplayName("등록되지 않은 코스를 삭제 시도시 예외가 발생한다.")
  public void removeNotExistedCourseTest() throws Exception {

    when(courseRepository.findById(any())).thenReturn(Optional.empty());

    assertThrows(CourseNotFoundException.class, () -> {
      myCourseService.removeCourse(1L, 1L);
    });
  }

  @Test
  @DisplayName("사용자가 작성한 데이트 코스 목록을 반환한다")
  public void getDateCoursesMadeByUser() throws Exception {

    User user = createUser();
    Course course1 = new Course(user, "test1");
    Course course2 = new Course(user, "test2");
    Course course3 = new Course(user, "test3");
    List<Course> list = new ArrayList<>();
    list.add(course1);
    list.add(course2);
    list.add(course3);
    Pageable pageable = PageRequest.of(0, 3);
    Page<Course> page = new PageImpl<>(list, pageable, 3);

    when(courseRepository.findAllCourseByUserId(user.getId(), pageable)).thenReturn(page);
    Page<CourseDto> myCourse = myCourseService.getMyCourses(1L, pageable);

    assertEquals(myCourse.getContent().size(), 3);
  }

  @Test
  @DisplayName("사용자가 작성한 코스를 삭제한다.")
  public void removeMyCourseTest() throws Exception {

    Course mockedCourse = mock(Course.class);

    when(courseRepository.findByIdAndUserId(any(), any())).thenReturn(
        Optional.ofNullable(mockedCourse));

    myCourseService.removeMyCourse(any(), any());

    verify(courseRepository).delete(any());
  }

  @Test
  @DisplayName("자신이 작성한 코스에 존재하지 않는 코스를 삭제 시도시 예외가 발생한다.")
  public void removeMyCourseNotExistedTest() throws Exception {

    when(courseRepository.findByIdAndUserId(any(), any())).thenReturn(Optional.empty());

    assertThrows(CourseNotFoundException.class, () -> {
      myCourseService.removeMyCourse(any(), any());
    });
  }

  @Test
  @DisplayName("사용자가 저장한 데이트 코스 목록을 반환한다")
  public void getUserSavedDateCourses() throws Exception {

    User user = createUser();
    Course course = new Course(user, "test");
    UserCourseSave userCourseSave = new UserCourseSave(user, course);
    List<UserCourseSave> list = new ArrayList<>();
    list.add(userCourseSave);
    Pageable pageable = PageRequest.of(0, 1);
    Page<UserCourseSave> page = new PageImpl<>(list, pageable, 1);

    when(userCourseSaveRepository.findAllUserCourseSavePageByUserId(user.getId(), pageable))
        .thenReturn(page);

    Page<SavedCourseDto> savedCourseListResult = myCourseService
        .getSavedCourses(1L, pageable);

    assertThat(savedCourseListResult.getContent()).size().isEqualTo(1);
  }
}