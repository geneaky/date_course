package me.toy.server.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import me.toy.server.dto.course.CourseResponseDto.RecentCourseDto;
import me.toy.server.dto.user.UserRequestDto.AddFollowerRequest;
import me.toy.server.dto.user.UserRequestDto.RemoveFollowerRequest;
import me.toy.server.dto.user.UserResponseDto.SavedCourseDto;
import me.toy.server.dto.user.UserResponseDto.UserDto;
import me.toy.server.dto.user.UserResponseDto.UserFollowers;
import me.toy.server.dto.user.UserResponseDto.UserFollowings;
import me.toy.server.entity.Course;
import me.toy.server.entity.Follow;
import me.toy.server.entity.User;
import me.toy.server.entity.UserCourseLike;
import me.toy.server.entity.UserCourseSave;
import me.toy.server.entity.UserFollow;
import me.toy.server.exception.user.UserNotFoundException;
import me.toy.server.repository.CourseRepository;
import me.toy.server.repository.FollowRepository;
import me.toy.server.repository.UserCourseSaveRepository;
import me.toy.server.repository.UserFollowRepository;
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
public class UserServiceTest {

  @Mock
  UserRepository userRepository;

  @Mock
  CourseRepository courseRepository;

  @Mock
  UserCourseSaveRepository userCourseSaveRepository;

  @Mock
  FollowRepository followRepository;

  @Mock
  UserFollowRepository userFollowRepository;

  @InjectMocks
  UserService userService;


  @Test
  @DisplayName("로그인하지 않은 사용자의 정보 요청시 예외가 발생한다")
  public void throwUserNotFoundExceptionWhenUserNotLogin() throws Exception {

    assertThrows(UserNotFoundException.class, () -> {
      userService.getUserInfo("");
    });
  }

  @Test
  @DisplayName("로그인한 사용자의 필요 정보를 반환한다.")
  public void getLoginUserInfo() throws Exception {

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

    UserDto userDto = userService.getUserInfo("test@naver.com");

    assertEquals(userDto.getEmail(), "test@naver.com");
    verify(userRepository, atLeast(1)).findByEmail(any());
  }

  @Test
  @DisplayName("사용자가 좋아요 누른 코스의 ID 목록을 반환한다")
  public void getUserLikedDateCourseIDs() throws Exception {

    String userEmail = "test@naver.com";
    User user = new User();
    user.setEmail(userEmail);
    Course course = new Course(user, "test");
    List<UserCourseLike> userCourseLikes = new ArrayList<>();
    UserCourseLike userCourseLike = new UserCourseLike(user, course);
    userCourseLikes.add(userCourseLike);
    user.setUserCourseLikes(userCourseLikes);
    userRepository.save(user);
    when(userRepository.findByEmail(userEmail))
        .thenReturn(Optional.of(user));

    List<Long> testLikedCourse = user.getUserCourseLikes()
        .stream()
        .map(testLike -> testLike.getCourse().getId())
        .collect(Collectors.toList());
    List<Long> likedCourse = userService.getLikedCourseIds(userEmail);

    assertEquals(testLikedCourse, likedCourse);
    verify(userRepository, atLeast(1)).findByEmail(any());
  }

  @Test
  @DisplayName("사용자가 좋아요 누른 코스가 없을시 빈 목록을 반환한다")
  public void whenNoUserLikedDateCourseThenReturnEmptyList() throws Exception {

    String userEmail = "test@naver.com";
    User user = new User();
    user.setEmail(userEmail);
    List<UserCourseLike> userCourseLikes = new ArrayList<>();
    user.setUserCourseLikes(userCourseLikes);
    userRepository.save(user);

    when(userRepository.findByEmail(userEmail))
        .thenReturn(Optional.of(user));
    List<Long> likedCourse = userService.getLikedCourseIds(userEmail);

    assertEquals(userCourseLikes, likedCourse);
    verify(userRepository, atLeast(1)).findByEmail(any());
  }

  @Test
  @DisplayName("사용자가 저장할 코스를 등록한다.")
  public void registDateCourseUserWantToSave() throws Exception {

    String userEmail = "test@naver.com";
    User user = new User();
    user.setEmail(userEmail);
    Course course = new Course(user, "test");
    UserCourseSave userCourseSave = new UserCourseSave(user, course);

    when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
    when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));

    userService.addCourse(course.getId(), userEmail);

    verify(userRepository).findByEmail(userEmail);
    verify(courseRepository).findById(any());
    verify(userCourseSaveRepository).save(any());
  }

  @Test
  @DisplayName("사용자가 저장한 데이트 코스의 ID 목록을 반환한다")
  public void getUserSavedDateCourseIDs() throws Exception {

    String userEmail = "test@naver.com";
    User user = new User();
    user.setEmail(userEmail);
    Course course = new Course(user, "test");
    UserCourseSave userCourseSave = new UserCourseSave(user, course);
    List<UserCourseSave> userCourseSaveList = new ArrayList<>();
    userCourseSaveList.add(userCourseSave);
    user.setUserCourseSaves(userCourseSaveList);

    when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
    List<Long> resultSavedCourse = userService.getSavedCourseIds(userEmail);
    List<Long> testSavedCourseList = user.getUserCourseSaves()
        .stream()
        .map(testSavedCourse -> testSavedCourse.getCourse().getId())
        .collect(Collectors.toList());

    assertEquals(resultSavedCourse, testSavedCourseList);
    verify(userRepository, atLeast(1)).findByEmail(any());
  }

  @Test
  @DisplayName("사용자가 저장한 데이트 코스가 없을시 빈 목록을 반환한다")
  public void whenNoUserSavedDateCourseThenReturnEmptyList() throws Exception {

    String userEmail = "test@naver.com";
    User user = new User();
    user.setEmail(userEmail);
    List<Long> testSavedCourse = new ArrayList<>();

    when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
    List<Long> savedCourse = userService.getSavedCourseIds(userEmail);

    assertEquals(savedCourse, testSavedCourse);
    verify(userRepository, atLeast(1)).findByEmail(any());
  }

  @Test
  @DisplayName("사용자가 지정한 데이트 코스를 저장한 데이트 코스에서 삭제한다")
  public void deleteDateCourseAtUserSavedDateCourses() throws Exception {

    String userEmail = "test@naver.com";
    User user = new User();
    user.setEmail(userEmail);
    Course course = new Course(user, "test");

    when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
    when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));
    userService.removeCourse(course.getId(), userEmail);

    verify(userRepository, atLeast(1)).findByEmail(any());
    verify(courseRepository, atLeast(1)).findById(any());
    verify(userCourseSaveRepository, times(1)).deleteByUserIdAndCourseId(any(), any());
  }

  @Test
  @DisplayName("사용자가 작성한 데이트 코스 목록을 반환한다")
  public void getDateCoursesMadeByUser() throws Exception {

    String userEmail = "test@naver.com";
    User user = new User();
    user.setEmail(userEmail);
    Course course1 = new Course(user, "test1");
    Course course2 = new Course(user, "test2");
    Course course3 = new Course(user, "test3");
    List<Course> list = new ArrayList<>();
    list.add(course1);
    list.add(course2);
    list.add(course3);
    Pageable pageable = PageRequest.of(0, 3);
    Page<Course> page = new PageImpl<>(list, pageable, 3);

    when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
    when(courseRepository.findAllCourseByUserId(user.getId(), pageable)).thenReturn(page);
    Page<RecentCourseDto> myCourse = userService.getMyCourses(userEmail, pageable);

    verify(userRepository, times(1)).findByEmail(userEmail);
    assertEquals(myCourse.getContent().size(), 3);
  }

  @Test
  @DisplayName("사용자가 저장한 데이트 코스 목록을 반환한다")
  public void getUserSavedDateCourses() throws Exception {

    String userEmail = "test@naver.com";
    User user = new User();
    user.setEmail(userEmail);
    Course course = new Course(user, "test");
    UserCourseSave userCourseSave = new UserCourseSave(user, course);
    List<UserCourseSave> list = new ArrayList<>();
    list.add(userCourseSave);
    Pageable pageable = PageRequest.of(0, 1);
    Page<UserCourseSave> page = new PageImpl<>(list, pageable, 1);

    when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
    when(userCourseSaveRepository.findAllUserCourseSavePageByUserId(user.getId(), pageable))
        .thenReturn(page);
    Page<SavedCourseDto> savedCourseListResult = userService
        .getSavedCourses(userEmail, pageable);

    verify(userRepository, times(1)).findByEmail(userEmail);
    assertEquals(savedCourseListResult.getContent().size(), 1);
  }

  @Test
  @DisplayName("사용자 팔로우를 성공")
  public void addFollowerInUserFollowersTest() throws Exception {

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

    when(userRepository.findByEmail("test@naver.com")).thenReturn(Optional.of(user));
    when(userRepository.findById(targetUser.getId())).thenReturn(Optional.of(targetUser));

    userService.followUser(addFollowerRequest, user.getEmail());

    verify(followRepository, times(1)).save(any());
    verify(userFollowRepository, times(1)).save(any());
    assertThat(user.getUserFollows().size()).isEqualTo(1);
  }

  @Test
  @DisplayName("팔로우하는 사용자들을 반환")
  public void getUserFollowingUsersTest() throws Exception {

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

    when(userRepository.findByEmail("test@naver.com")).thenReturn(Optional.of(user));
    when(userRepository.findAllFollowings("test@naver.com")).thenReturn(users);

    UserFollowings userFollowingUsers = userService.getUserFollowings("test@naver.com");

    assertThat(userFollowingUsers.getFollowingUserDtos().size()).isEqualTo(2);
  }

  @Test
  @DisplayName("사용자 팔로우 목록에서 선택한 팔로우 사용자를 언팔로우")
  public void removeFollowerInUserFollowersTest() throws Exception {

    List<UserFollow> userFollows = new ArrayList<>();
    User followUser = User.builder()
        .id(3L)
        .build();
    User user = User.builder()
        .email("test@naver.com")
        .userFollows(userFollows)
        .build();
    Follow follow = new Follow(followUser.getId());
    UserFollow userFollow = new UserFollow(user, follow);

    RemoveFollowerRequest removeFollowerRequest = new RemoveFollowerRequest(
        follow.getFollowUserId());

    when(userRepository.findByEmail("test@naver.com")).thenReturn(Optional.of(user));
    when(userRepository.findById(follow.getFollowUserId())).thenReturn(Optional.of(followUser));
    when(userFollowRepository.isFollow(user.getId(), follow.getFollowUserId())).thenReturn(true);
    userService.unfollowUser(removeFollowerRequest, "test@naver.com");

    verify(userFollowRepository, times(1)).deleteUserFollow(any(), any());
    verify(followRepository, times(1)).deleteFollow(any(), any());
  }

  @Test
  @DisplayName("사용자를 팔로우하는 팔루워 목록 반환")
  public void getUserFollowersUsersTest() throws Exception {

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

    when(userRepository.findByEmail("test@naver.com")).thenReturn(Optional.ofNullable(user));
    when(userRepository.findAllFollowers(1L)).thenReturn(list);

    UserFollowers userFollowersUsers = userService.getUserFollowers("test@naver.com");

    assertThat(userFollowersUsers.getFollowerUserDtos().size()).isEqualTo(2);
    assertThat(user.getUserFollows().size()).isEqualTo(2);
  }
}