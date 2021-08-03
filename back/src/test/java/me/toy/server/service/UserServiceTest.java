package me.toy.server.service;

import me.toy.server.dto.DateCourseResponseDto.RecentDateCourseDto;
import me.toy.server.dto.UserResponseDto.SavedDateCourseDto;
import me.toy.server.dto.UserResponseDto.UserDto;
import me.toy.server.entity.DateCourse;
import me.toy.server.entity.UserDateCourseLike;
import me.toy.server.entity.UserDateCourseSave;
import me.toy.server.entity.User;
import me.toy.server.exception.UserNotFoundException;
import me.toy.server.repository.DateCourseRepository;
import me.toy.server.repository.UserDateCourseSaveRepository;
import me.toy.server.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @Mock
  UserRepository userRepository;

  @Mock
  DateCourseRepository dateCourseRepository;

  @Mock
  UserDateCourseSaveRepository userDateCourseSaveRepository;

  @InjectMocks
  UserService userService;


  @Test
  @DisplayName("로그인하지 않은 사용자의 정보 요청시 예외가 발생한다")
  public void throwUserNotFoundExceptionWhenUserNotLogin() throws Exception {
    //given
    //when
    assertThrows(UserNotFoundException.class, () -> {
      userService.findUser("");
    });
    //then
  }

  @Test
  @DisplayName("로그인한 사용자의 필요 정보를 반환한다.")
  public void getLoginUserInfo() throws Exception {
    //given
    User user = User.builder()
        .id(1L)
        .email("test@naver.com")
        .name("user01")
        .password("nopassword")
        .build();
    Optional<User> oUser = Optional.of(user);
    userRepository.save(user);
    when(userRepository.findByEmail("test@naver.com"))
        .thenReturn(oUser);
    //when
    UserDto userDto = userService.findUser("test@naver.com");
    //then
    assertEquals(userDto.getEmail(), "test@naver.com");
    verify(userRepository, atLeast(1)).findByEmail(any());
  }

  @Test
  @DisplayName("사용자가 좋아요 누른 코스의 ID 목록을 반환한다")
  public void getUserLikedDateCourseIDs() throws Exception {
    //given
    String userEmail = "test@naver.com";
    User user = new User();
    user.setEmail(userEmail);
    DateCourse dateCourse = new DateCourse(user, "test");
    List<UserDateCourseLike> userDateCourseLikes = new ArrayList<>();
    UserDateCourseLike userDateCourseLike = new UserDateCourseLike(user, dateCourse);
    userDateCourseLikes.add(userDateCourseLike);
    user.setUserDateCourseLikes(userDateCourseLikes);
    userRepository.save(user);
    when(userRepository.findByEmail(userEmail))
        .thenReturn(Optional.of(user));
    //when
    List<Long> testLikedCourse = user.getUserDateCourseLikes()
        .stream()
        .map(testLike -> testLike.getDateCourse().getId())
        .collect(Collectors.toList());
    List<Long> likedCourse = userService.findLikedCourseIds(userEmail);
    //then
    assertEquals(testLikedCourse, likedCourse);
    verify(userRepository, atLeast(1)).findByEmail(any());
  }

  @Test
  @DisplayName("사용자가 좋아요 누른 코스가 없을시 빈 목록을 반환한다")
  public void whenNoUserLikedDateCourseThenReturnEmptyList() throws Exception {
    //given
    String userEmail = "test@naver.com";
    User user = new User();
    user.setEmail(userEmail);
    List<UserDateCourseLike> userDateCourseLikes = new ArrayList<>();
    user.setUserDateCourseLikes(userDateCourseLikes);
    userRepository.save(user);
    //when
    when(userRepository.findByEmail(userEmail))
        .thenReturn(Optional.of(user));
    List<Long> likedCourse = userService.findLikedCourseIds(userEmail);
    //then
    assertEquals(userDateCourseLikes, likedCourse);
    verify(userRepository, atLeast(1)).findByEmail(any());
  }

  @Test
  @DisplayName("사용자가 저장할 코스를 등록한다.")
  public void registDateCourseUserWantToSave() throws Exception {
    //given
    String userEmail = "test@naver.com";
    User user = new User();
    user.setEmail(userEmail);
    DateCourse dateCourse = new DateCourse(user, "test");
    UserDateCourseSave userDateCourseSave = new UserDateCourseSave(user, dateCourse);
    //when
    when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
    when(dateCourseRepository.findById(dateCourse.getId())).thenReturn(Optional.of(dateCourse));
    Long savedCourseId = userService.registSavedCourse(dateCourse.getId(), userEmail);
    //then
    assertEquals(savedCourseId, userDateCourseSave.getId());
    verify(userRepository, atLeast(1)).findByEmail(any());
    verify(dateCourseRepository, atLeast(1)).findById(any());
  }

  @Test
  @DisplayName("사용자가 저장한 데이트 코스의 ID 목록을 반환한다")
  public void getUserSavedDateCourseIDs() throws Exception {
    //given
    String userEmail = "test@naver.com";
    User user = new User();
    user.setEmail(userEmail);
    DateCourse dateCourse = new DateCourse(user, "test");
    UserDateCourseSave userDateCourseSave = new UserDateCourseSave(user, dateCourse);
    List<UserDateCourseSave> userDateCourseSaveList = new ArrayList<>();
    userDateCourseSaveList.add(userDateCourseSave);
    user.setUserDateCoursSaves(userDateCourseSaveList);
    //when
    when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
    List<Long> resultSavedCourse = userService.findSavedCourseIds(userEmail);
    List<Long> testSavedCourseList = user.getUserDateCoursSaves()
        .stream()
        .map(testSavedCourse -> testSavedCourse.getDateCourse().getId())
        .collect(Collectors.toList());
    //then
    assertEquals(resultSavedCourse, testSavedCourseList);
    verify(userRepository, atLeast(1)).findByEmail(any());
  }

  @Test
  @DisplayName("사용자가 저장한 데이트 코스가 없을시 빈 목록을 반환한다")
  public void whenNoUserSavedDateCourseThenReturnEmptyList() throws Exception {
    //given
    String userEmail = "test@naver.com";
    User user = new User();
    user.setEmail(userEmail);
    List<Long> testSavedCourse = new ArrayList<>();
    //when
    when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
    List<Long> savedCourse = userService.findSavedCourseIds(userEmail);
    //then
    assertEquals(savedCourse, testSavedCourse);
    verify(userRepository, atLeast(1)).findByEmail(any());
  }

  @Test
  @DisplayName("사용자가 지정한 데이트 코스를 저장한 데이트 코스에서 삭제한다")
  public void deleteDateCourseAtUserSavedDateCourses() throws Exception {
    //given
    String userEmail = "test@naver.com";
    User user = new User();
    user.setEmail(userEmail);
    DateCourse dateCourse = new DateCourse(user, "test");
    UserDateCourseSave userDateCourseSave = new UserDateCourseSave(user, dateCourse);
    List<UserDateCourseSave> userDateCourseSaveList = new ArrayList<>();
    userDateCourseSaveList.add(userDateCourseSave);
    user.setUserDateCoursSaves(userDateCourseSaveList);
    //when
    when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
    when(dateCourseRepository.findById(dateCourse.getId())).thenReturn(Optional.of(dateCourse));
    userService.deleteSavedCourse(dateCourse.getId(), userEmail);
    //then
    assertEquals(user.getUserDateCoursSaves().size(), 0);
    verify(userRepository, atLeast(1)).findByEmail(any());
    verify(dateCourseRepository, atLeast(1)).findById(any());
  }

  @Test
  @DisplayName("사용자가 작성한 데이트 코스 목록을 반환한다")
  public void getDateCoursesMadeByUser() throws Exception {
    //given
    String userEmail = "test@naver.com";
    User user = new User();
    user.setEmail(userEmail);
    DateCourse dateCourse1 = new DateCourse(user, "test1");
    DateCourse dateCourse2 = new DateCourse(user, "test2");
    DateCourse dateCourse3 = new DateCourse(user, "test3");
    List<DateCourse> dateCourseList = new ArrayList<>();
    dateCourseList.add(dateCourse1);
    dateCourseList.add(dateCourse2);
    dateCourseList.add(dateCourse3);
    user.setDateCourses(dateCourseList);
    List<RecentDateCourseDto> testMyCourse = new ArrayList<>();
    testMyCourse.add(new RecentDateCourseDto(dateCourse1));
    testMyCourse.add(new RecentDateCourseDto(dateCourse2));
    testMyCourse.add(new RecentDateCourseDto(dateCourse3));
    //when
    when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
    when(dateCourseRepository.findAllDateCourseByUserId(user.getId())).thenReturn(testMyCourse);
    List<RecentDateCourseDto> myCourse = userService.findMyCourse(userEmail);
    //then
    assertEquals(myCourse.size(), 3);
  }

  @Test
  @DisplayName("사용자가 저장한 데이트 코스 목록을 반환한다")
  public void getUserSavedDateCourses() throws Exception {
    //given
    String userEmail = "test@naver.com";
    User user = new User();
    user.setEmail(userEmail);
    DateCourse dateCourse = new DateCourse(user, "test");
    UserDateCourseSave userDateCourseSave = new UserDateCourseSave(user, dateCourse);
    List<UserDateCourseSave> userDateCourseSaveList = new ArrayList<>();
    userDateCourseSaveList.add(userDateCourseSave);
    user.setUserDateCoursSaves(userDateCourseSaveList);
    List<SavedDateCourseDto> testSavedCourse = new ArrayList<>();
    testSavedCourse.add(new SavedDateCourseDto(userDateCourseSave));
    //when
    when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
    when(dateCourseRepository.findAllSavedCourseByUserId(user.getId())).thenReturn(testSavedCourse);
    List<SavedDateCourseDto> savedCourseListResult = userService.findSavedCourseList(userEmail);
    //then
    assertEquals(savedCourseListResult.size(), 1);
  }
}