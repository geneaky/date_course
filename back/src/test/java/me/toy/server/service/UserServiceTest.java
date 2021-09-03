package me.toy.server.service;

import me.toy.server.dto.DateCourseResponseDto.RecentDateCourseDto;
import me.toy.server.dto.UserRequestDto.AddFollowerRequest;
import me.toy.server.dto.UserRequestDto.RemoveFollowerRequest;
import me.toy.server.dto.UserResponseDto.SavedDateCourseDto;
import me.toy.server.dto.UserResponseDto.UserDto;
import me.toy.server.dto.UserResponseDto.UserFollowers;
import me.toy.server.dto.UserResponseDto.UserFollowings;
import me.toy.server.entity.DateCourse;
import me.toy.server.entity.Follow;
import me.toy.server.entity.UserDateCourseLike;
import me.toy.server.entity.UserDateCourseSave;
import me.toy.server.entity.User;
import me.toy.server.entity.UserFollow;
import me.toy.server.exception.user.UserNotFoundException;
import me.toy.server.repository.DateCourseRepository;
import me.toy.server.repository.FollowRepository;
import me.toy.server.repository.UserDateCourseSaveRepository;
import me.toy.server.repository.UserFollowRepository;
import me.toy.server.repository.UserRepository;
import org.assertj.core.api.Assertions;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.*;
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

  @Mock
  FollowRepository followRepository;

  @Mock
  UserFollowRepository userFollowRepository;

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
    //when
    when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
    when(dateCourseRepository.findById(dateCourse.getId())).thenReturn(Optional.of(dateCourse));
    userService.deleteSavedCourse(dateCourse.getId(), userEmail);
    //then
    verify(userRepository, atLeast(1)).findByEmail(any());
    verify(dateCourseRepository, atLeast(1)).findById(any());
    verify(userDateCourseSaveRepository, times(1)).deleteByUserIdAndDateCourseId(any(), any());
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
    List<DateCourse> list = new ArrayList<>();
    list.add(dateCourse1);
    list.add(dateCourse2);
    list.add(dateCourse3);
    Pageable pageable = PageRequest.of(0, 3);
    Page<DateCourse> page = new PageImpl<DateCourse>(list, pageable, 3);
    //when
    when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
    when(dateCourseRepository.findAllDateCourseByUserId(user.getId(), pageable)).thenReturn(page);
    Page<RecentDateCourseDto> myCourse = userService.findMyCourse(userEmail, pageable);
    //then
    verify(userRepository, times(1)).findByEmail(userEmail);
    assertEquals(myCourse.getContent().size(), 3);
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
    List<UserDateCourseSave> list = new ArrayList<>();
    list.add(userDateCourseSave);
    Pageable pageable = PageRequest.of(0, 1);
    Page<UserDateCourseSave> page = new PageImpl<>(list, pageable, 1);

    //when
    when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
    when(userDateCourseSaveRepository.findAllUserDateCourseSaveByUserId(user.getId(), pageable))
        .thenReturn(page);
    Page<SavedDateCourseDto> savedCourseListResult = userService
        .findSavedCourseList(userEmail, pageable);
    //then
    verify(userRepository, times(1)).findByEmail(userEmail);
    assertEquals(savedCourseListResult.getContent().size(), 1);
  }

  @Test
  @DisplayName("사용자 팔로우를 성공")
  public void addFollowerInUserFollowersTest() throws Exception {
    //given
    List<UserFollow> list = new ArrayList<>();
    User user = User.builder()
        .email("test@naver.com")
        .userFollows(list)
        .build();
    User targetUser = User.builder()
        .id(3L)
        .email("target@naver.com")
        .build();
    AddFollowerRequest addFollowerRequest = new AddFollowerRequest(targetUser.getId());

    //when
    when(userRepository.findByEmail("test@naver.com")).thenReturn(Optional.of(user));
    when(userRepository.findById(targetUser.getId())).thenReturn(Optional.of(targetUser));
    userService.addFollowerInUserFollowers(addFollowerRequest, user.getEmail());
    //then
    verify(followRepository, times(1)).save(any());
    verify(userFollowRepository, times(1)).save(any());
    assertThat(user.getUserFollows().size()).isEqualTo(1);
  }

  @Test
  @DisplayName("팔로우하는 사용자들을 반환")
  public void getUserFollowingUsersTest() throws Exception {
    //given
    List<UserFollow> list = new ArrayList<>();
    User user = User.builder()
        .email("test@naver.com")
        .userFollows(list)
        .build();
    List<User> users = new ArrayList<>();
    User user1 = User.builder()
        .email("other@naver.com")
        .build();
    User user2 = User.builder()
        .email("people@naver.com")
        .build();
    users.add(user1);
    users.add(user2);
    //when
    when(userRepository.findByEmail("test@naver.com")).thenReturn(Optional.of(user));
    when(userRepository.findAllFollowingUsers("test@naver.com")).thenReturn(users);
    UserFollowings userFollowingUsers = userService.getUserFollowingUsers("test@naver.com");
    //then
    assertThat(userFollowingUsers.getFollowingUserDtos().size()).isEqualTo(2);
  }

  @Test
  @DisplayName("사용자 팔로우 목록에서 선택한 팔로우 사용자를 언팔로우")
  public void removeFollowerInUserFollowersTest() throws Exception {
    //given
    List<UserFollow> userFollows = new ArrayList<>();
    Follow follow = new Follow(3L);
    User user = User.builder()
        .email("test@naver.com")
        .userFollows(userFollows)
        .build();
    UserFollow userFollow = new UserFollow(user, follow);

    RemoveFollowerRequest removeFollowerRequest = new RemoveFollowerRequest(
        follow.getFollowUserId());
    //when
    when(userRepository.findByEmail("test@naver.com")).thenReturn(Optional.ofNullable(user));
    userService.removeFollowerInUserFollowers(removeFollowerRequest, "test@naver.com");
    //then
    verify(userFollowRepository, times(1)).deleteUserFollowInUserFollowings(any(), any());
    verify(followRepository, times(1)).deleteFollowInUserFollowings(any(), any());
  }

  @Test
  @DisplayName("사용자를 팔로우하는 팔루워 목록 반환")
  public void getUserFollowersUsersTest() throws Exception {
    //given
    User user1 = User.builder()
        .id(2L)
        .email("other@naver.com")
        .build();
    User user2 = User.builder()
        .id(3L)
        .email("people@naver.com")
        .build();
    List<UserFollow> userFollows = new ArrayList<>();
    Follow follow1 = new Follow(2L);
    Follow follow2 = new Follow(3L);
    User user = User.builder()
        .id(1L)
        .userFollows(userFollows)
        .email("test@naver.com")
        .build();
    UserFollow userFollow1 = new UserFollow(user, follow1);
    UserFollow userFollow2 = new UserFollow(user, follow2);
    List<User> list = new ArrayList<>();
    list.add(user1);
    list.add(user2);
    //when
    when(userRepository.findByEmail("test@naver.com")).thenReturn(Optional.ofNullable(user));
    when(userRepository.findAllFollowerUsers(1L)).thenReturn(list);
    UserFollowers userFollowersUsers = userService.getUserFollowersUsers("test@naver.com");
    //then
    assertThat(userFollowersUsers.getFollowerUserDtos().size()).isEqualTo(2);
    assertThat(user.getUserFollows().size()).isEqualTo(2);
  }
}