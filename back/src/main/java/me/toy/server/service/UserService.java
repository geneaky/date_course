package me.toy.server.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import me.toy.server.dto.DateCourseResponseDto.RecentDateCourseDto;
import me.toy.server.dto.UserRequestDto.AddFollowerRequest;
import me.toy.server.dto.UserRequestDto.RemoveFollowerRequest;
import me.toy.server.dto.UserRequestDto.UserRegisterForm;
import me.toy.server.dto.UserResponseDto.FollowerUserDto;
import me.toy.server.dto.UserResponseDto.FollowingUserDto;
import me.toy.server.dto.UserResponseDto.SavedDateCourseDto;
import me.toy.server.dto.UserResponseDto.UserDto;
import me.toy.server.dto.UserResponseDto.UserFollowers;
import me.toy.server.dto.UserResponseDto.UserFollowings;
import me.toy.server.entity.DateCourse;
import me.toy.server.entity.Follow;
import me.toy.server.entity.User;
import me.toy.server.entity.UserDateCourseSave;
import me.toy.server.entity.UserFollow;
import me.toy.server.exception.datecourse.DateCourseNotFoundException;
import me.toy.server.exception.user.EmailDuplicationException;
import me.toy.server.exception.user.UserNotFoundException;
import me.toy.server.repository.DateCourseRepository;
import me.toy.server.repository.FollowRepository;
import me.toy.server.repository.UserDateCourseSaveRepository;
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
  private final DateCourseRepository dateCourseRepository;
  private final UserDateCourseSaveRepository userDateCourseSaveRepository;
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

  @Transactional(readOnly = true)
  public List<Long> getLikedCourseIds(String userEmail) {

    User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
        new UserNotFoundException("그런 이메일로 가입한 사용자는 없습니다.")
    );
//쿼리 확인 point -> batch size 적용되는지 확인
    return user.getUserDateCourseLikes()
        .stream()
        .map(like -> like.getDateCourse().getId())
        .collect(Collectors.toList());
  }

  @Transactional
  public void addCourse(Long courseId, String userEmail) {

    User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
        new UserNotFoundException("그런 이메일로 가입한 사용자는 없습니다.")
    );
    DateCourse dateCourse = dateCourseRepository.findById(courseId).orElseThrow(() ->
        new DateCourseNotFoundException("찾으시는 데이트 코스는 없습니다."));
    UserDateCourseSave userDateCourseSave = new UserDateCourseSave(user, dateCourse);

    userDateCourseSaveRepository.save(userDateCourseSave);
  }

  @Transactional
  public void removeCourse(Long courseId, String userEmail) {

    User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
        new UserNotFoundException("그런 이메일로 가입한 사용자는 없습니다."));
    DateCourse dateCourse = dateCourseRepository.findById(courseId).orElseThrow(() ->
        new DateCourseNotFoundException("찾으시는 데이트 코스는 없습니다."));

    userDateCourseSaveRepository.deleteByUserIdAndDateCourseId(user.getId(), dateCourse.getId());
  }

  @Transactional(readOnly = true)
  public Page<SavedDateCourseDto> getSavedCourses(String userEmail, Pageable pageable) {

    User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
        new UserNotFoundException("그런 이메일로 가입한 사용자는 없습니다."));

    Page<UserDateCourseSave> allSavedCourseByUserId = userDateCourseSaveRepository
        .findAllUserDateCourseSaveByUserId(user.getId(), pageable);
    return allSavedCourseByUserId.map(SavedDateCourseDto::new);
  }

  @Transactional(readOnly = true)
  public List<Long> getSavedCourseIds(String userEmail) {

    User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
        new UserNotFoundException("그런 이메일로 가입한 사용자는 없습니다."));

//    쿼리 확인 point
    return user.getUserDateCoursSaves()
        .stream()
        .map(savedCourse -> savedCourse.getDateCourse().getId())
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public Page<RecentDateCourseDto> getMyCourses(String userEmail, Pageable pageable) {

    User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
        new UserNotFoundException("그런 이메일로 가입한 사용자는 없습니다."));

    Page<DateCourse> allDateCourseByUserId = dateCourseRepository
        .findAllDateCourseByUserId(user.getId(), pageable);
    return allDateCourseByUserId.map(RecentDateCourseDto::new);
  }

  @Transactional
  public void removeMyCourse(Long courseId, String userEmail) {

    userRepository.findByEmail(userEmail).orElseThrow(() ->
        new UserNotFoundException("그런 이메일로 가입한 사용자는 없습니다."));
    DateCourse dateCourse = dateCourseRepository.findById(courseId).orElseThrow(() ->
        new DateCourseNotFoundException("찾으시는 데이트 코스는 없습니다."));

    dateCourseRepository.delete(dateCourse);
  }

  @Transactional
  public void followUser(AddFollowerRequest addFollowerRequest, String userEmail) {

    User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
        new UserNotFoundException("그런 이메일로 가입한 사용자는 없습니다."));
    userRepository.findById(addFollowerRequest.getFollowerId())
        .orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자 입니다."));
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
    userFollowRepository
        .deleteUserFollowInUserFollowings(user.getId(), removeFollowerRequest.getFollowerId());
    followRepository
        .deleteFollowInUserFollowings(user.getId(), removeFollowerRequest.getFollowerId());
  }

  @Transactional(readOnly = true)
  public UserFollowings getUserFollowings(
      String userEmail) {

    userRepository.findByEmail(userEmail).orElseThrow(() ->
        new UserNotFoundException("그런 이메일로 가입한 사용자는 없습니다."));

    List<User> allFollowingUsers = userRepository.findAllFollowingUsers(userEmail);

    List<FollowingUserDto> followingUserDtos = allFollowingUsers.stream()
        .map(u -> new FollowingUserDto(u.getId(), u.getName(), u.getEmail())).collect(
            Collectors.toList());
    return new UserFollowings(followingUserDtos);
  }

  @Transactional(readOnly = true)
  public UserFollowers getUserFollowers(String userEmail) {

    User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
        new UserNotFoundException("그런 이메일로 가입한 사용자는 없습니다."));

    List<User> allFollowerUsers = userRepository.findAllFollowerUsers(user.getId());

    List<FollowerUserDto> followerUserDtos = allFollowerUsers.stream()
        .map(u -> new FollowerUserDto(u.getId(), u.getName(), u.getEmail()))
        .collect(Collectors.toList());

    return new UserFollowers(followerUserDtos);
  }
}
