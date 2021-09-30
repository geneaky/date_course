package me.toy.server.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import me.toy.server.dto.course.CourseResponseDto.RecentCourseDto;
import me.toy.server.dto.user.UserRequestDto.AddFollowerRequest;
import me.toy.server.dto.user.UserRequestDto.RemoveFollowerRequest;
import me.toy.server.dto.user.UserRequestDto.UserRegisterForm;
import me.toy.server.dto.user.UserResponseDto.FollowerUserDto;
import me.toy.server.dto.user.UserResponseDto.FollowingUserDto;
import me.toy.server.dto.user.UserResponseDto.SavedCourseDto;
import me.toy.server.dto.user.UserResponseDto.UserDto;
import me.toy.server.dto.user.UserResponseDto.UserFollowers;
import me.toy.server.dto.user.UserResponseDto.UserFollowings;
import me.toy.server.entity.Course;
import me.toy.server.entity.Follow;
import me.toy.server.entity.User;
import me.toy.server.entity.UserCourseSave;
import me.toy.server.entity.UserFollow;
import me.toy.server.exception.course.CourseNotFoundException;
import me.toy.server.exception.user.AlreadyFollowUserException;
import me.toy.server.exception.user.AlreadyUnfollowUserException;
import me.toy.server.exception.user.EmailDuplicationException;
import me.toy.server.exception.user.UserNotFoundException;
import me.toy.server.repository.CourseRepository;
import me.toy.server.repository.FollowRepository;
import me.toy.server.repository.UserCourseSaveRepository;
import me.toy.server.repository.UserFollowRepository;
import me.toy.server.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final CourseRepository courseRepository;
  private final UserCourseSaveRepository userCourseSaveRepository;
  private final UserFollowRepository userFollowRepository;
  private final FollowRepository followRepository;
  private final PasswordEncoder bCryptPasswordEncorder;

  @Transactional
  public void createUserAccount(UserRegisterForm userRegisterForm) {

    if (userRepository.findByEmail(userRegisterForm.getEmail()).isPresent()) {
      throw new EmailDuplicationException("해당 이메일로 이미 가입한 사용자 입니다.");
    }

    User newUser = User.builder()
        .email(userRegisterForm.getEmail())
        .password(bCryptPasswordEncorder.encode(userRegisterForm.getPassword()))
        .name(userRegisterForm.getNickName())
        .build();

    userRepository.save(newUser);
  }

  @Transactional(readOnly = true)
  public UserDto getUserInfo(String userEmail) {

    User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
        new UserNotFoundException("그런 이메일로 가입한 사용자는 없습니다.")
    );

    return new UserDto(user);
  }

  //  courseService의 역할임
  @Transactional(readOnly = true)
  public List<Long> getLikedCourseIds(String userEmail) {

    User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
        new UserNotFoundException("그런 이메일로 가입한 사용자는 없습니다.")
    );
//쿼리 확인 point -> batch size 적용되는지 확인
    return user.getUserCourseLikes()
        .stream()
        .map(like -> like.getCourse().getId())
        .collect(Collectors.toList());
  }

  //  courseService의 역할임 맞나? 새로운 서비스 ?
  @Transactional
  public void addCourse(Long courseId, String userEmail) {

    User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
        new UserNotFoundException("그런 이메일로 가입한 사용자는 없습니다.")
    );
    Course course = courseRepository.findById(courseId).orElseThrow(() ->
        new CourseNotFoundException("찾으시는 데이트 코스는 없습니다."));
    UserCourseSave userCourseSave = new UserCourseSave(user, course);

    userCourseSaveRepository.save(userCourseSave);
  }

  //  courseService의 역할임
  @Transactional
  public void removeCourse(Long courseId, String userEmail) {

    User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
        new UserNotFoundException("그런 이메일로 가입한 사용자는 없습니다."));
    Course course = courseRepository.findById(courseId).orElseThrow(() ->
        new CourseNotFoundException("찾으시는 데이트 코스는 없습니다."));

    userCourseSaveRepository.deleteByUserIdAndCourseId(user.getId(), course.getId());
  }

  //  courseService의 역할임
  @Transactional(readOnly = true)
  public Page<SavedCourseDto> getSavedCourses(String userEmail, Pageable pageable) {

    User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
        new UserNotFoundException("그런 이메일로 가입한 사용자는 없습니다."));

    Page<UserCourseSave> allSavedCourseByUserId = userCourseSaveRepository
        .findAllUserCourseSavePageByUserId(user.getId(), pageable);
    return allSavedCourseByUserId.map(SavedCourseDto::new);
  }

  //  courseService의 역할임
  @Transactional(readOnly = true)
  public List<Long> getSavedCourseIds(String userEmail) {

    User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
        new UserNotFoundException("그런 이메일로 가입한 사용자는 없습니다."));

//    쿼리 확인 point
    return user.getUserCourseSaves()
        .stream()
        .map(savedCourse -> savedCourse.getCourse().getId())
        .collect(Collectors.toList());
  }

  //  여긴 누구의 역할일까 사용자의 코스를 가져오는거면 userService일까 courseService일까
  @Transactional(readOnly = true)
  public Page<RecentCourseDto> getMyCourses(String userEmail, Pageable pageable) {

    User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
        new UserNotFoundException("그런 이메일로 가입한 사용자는 없습니다."));

    Page<Course> coursePage = courseRepository
        .findAllCourseByUserId(user.getId(), pageable);
    return coursePage.map(RecentCourseDto::new);
  }

  @Transactional
  public void removeMyCourse(Long courseId, String userEmail) {

    userRepository.findByEmail(userEmail).orElseThrow(() ->
        new UserNotFoundException("그런 이메일로 가입한 사용자는 없습니다."));
    Course course = courseRepository.findById(courseId).orElseThrow(() ->
        new CourseNotFoundException("찾으시는 데이트 코스는 없습니다."));

    courseRepository.delete(course);
  }

  @Transactional
  public void followUser(AddFollowerRequest addFollowerRequest, String userEmail) {

    User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
        new UserNotFoundException("그런 이메일로 가입한 사용자는 없습니다."));
    userRepository.findById(addFollowerRequest.getFollowerId())
        .orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자 입니다."));

    if (userFollowRepository.isFollow(user.getId(), addFollowerRequest.getFollowerId())) {
      throw new AlreadyFollowUserException("이미 팔로우 상태 입니다.");
    }
    Follow follow = new Follow(addFollowerRequest.getFollowerId());
    UserFollow userFollow = new UserFollow(user, follow);

    followRepository.save(follow);
    userFollowRepository.save(userFollow);
  }

  @Transactional
  public void unfollowUser(RemoveFollowerRequest removeFollowerRequest,
      String userEmail) {

    User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
        new UserNotFoundException("그런 이메일로 가입한 사용자는 없습니다."));

    if (!userFollowRepository.isFollow(user.getId(), removeFollowerRequest.getFollowerId())) {
      throw new AlreadyUnfollowUserException("해당 유저를 팔로우 하고 있지 않습니다.");
    }

    userFollowRepository
        .deleteUserFollow(user.getId(), removeFollowerRequest.getFollowerId());
    followRepository
        .deleteFollow(user.getId(), removeFollowerRequest.getFollowerId());
  }

  @Transactional(readOnly = true)
  public UserFollowings getUserFollowings(
      String userEmail) {

    userRepository.findByEmail(userEmail).orElseThrow(() ->
        new UserNotFoundException("그런 이메일로 가입한 사용자는 없습니다."));

    List<User> allFollowingUsers = userRepository.findAllFollowings(userEmail);

    List<FollowingUserDto> followingUserDtos = allFollowingUsers.stream()
        .map(u -> new FollowingUserDto(u.getId(), u.getName(), u.getEmail())).collect(
            Collectors.toList());
    return new UserFollowings(followingUserDtos);
  }

  @Transactional(readOnly = true)
  public UserFollowers getUserFollowers(String userEmail) {

    User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
        new UserNotFoundException("그런 이메일로 가입한 사용자는 없습니다."));

    List<User> allFollowerUsers = userRepository.findAllFollowers(user.getId());

    List<FollowerUserDto> followerUserDtos = allFollowerUsers.stream()
        .map(u -> new FollowerUserDto(u.getId(), u.getName(), u.getEmail()))
        .collect(Collectors.toList());

    return new UserFollowers(followerUserDtos);
  }
}
