package me.toy.server.service;

import lombok.RequiredArgsConstructor;
import me.toy.server.dto.DateCourseResponseDto.RecentDateCourseDto;
import me.toy.server.dto.UserRequestDto;
import me.toy.server.dto.UserRequestDto.AddFollower;
import me.toy.server.dto.UserResponseDto;
import me.toy.server.dto.UserResponseDto.FollowingUserDto;
import me.toy.server.dto.UserResponseDto.SavedDateCourseDto;
import me.toy.server.dto.UserResponseDto.UserDto;
import me.toy.server.dto.UserResponseDto.UserFollowingUsers;
import me.toy.server.entity.DateCourse;
import me.toy.server.entity.Follow;
import me.toy.server.entity.UserDateCourseSave;
import me.toy.server.entity.User;
import me.toy.server.entity.UserFollow;
import me.toy.server.exception.datecourse.DateCourseNotFoundException;
import me.toy.server.exception.user.UserNotFoundException;
import me.toy.server.repository.DateCourseRepository;
import me.toy.server.repository.FollowRepository;
import me.toy.server.repository.UserDateCourseSaveRepository;
import me.toy.server.repository.UserFollowRepository;
import me.toy.server.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final DateCourseRepository dateCourseRepository;
  private final UserDateCourseSaveRepository userDateCourseSaveRepository;
  private final UserFollowRepository userFollowRepository;
  private final FollowRepository followRepository;

  @Transactional(readOnly = true)
  public UserDto findUser(String userEmail) {

    User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
        new UserNotFoundException("그런 이메일로 가입한 사용자는 없습니다.")
    );

    return new UserDto(user);
  }

  @Transactional(readOnly = true)
  public List<Long> findLikedCourseIds(String userEmail) {

    User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
        new UserNotFoundException("그런 이메일로 가입한 사용자는 없습니다.")
    );

    return getLikedCourseIds(user);
  }

  private List<Long> getLikedCourseIds(User user) {

    return user.getUserDateCourseLikes()
        .stream()
        .map(like -> like.getDateCourse().getId())
        .collect(Collectors.toList());
  }

  public Long registSavedCourse(Long courseId, String userEmail) {

    User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
        new UserNotFoundException("그런 이메일로 가입한 사용자는 없습니다.")
    );
    DateCourse dateCourse = dateCourseRepository.findById(courseId).orElseThrow(() ->
        new DateCourseNotFoundException("찾으시는 데이트 코스는 없습니다."));
    UserDateCourseSave userDateCourseSave = new UserDateCourseSave(user, dateCourse);

    userDateCourseSaveRepository.save(userDateCourseSave);

    return userDateCourseSave.getId();
  }

  @Transactional(readOnly = true)
  public List<Long> findSavedCourseIds(String userEmail) {

    User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
        new UserNotFoundException("그런 이메일로 가입한 사용자는 없습니다."));

    return getSavedCourseIds(user);
  }

  private List<Long> getSavedCourseIds(User user) {

    return user.getUserDateCoursSaves()
        .stream()
        .map(savedCourse -> savedCourse.getDateCourse().getId())
        .collect(Collectors.toList());
  }

  @Transactional
  public void deleteSavedCourse(Long courseId, String userEmail) {

    User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
        new UserNotFoundException("그런 이메일로 가입한 사용자는 없습니다."));
    DateCourse dateCourse = dateCourseRepository.findById(courseId).orElseThrow(() ->
        new DateCourseNotFoundException("찾으시는 데이트 코스는 없습니다."));

    userDateCourseSaveRepository.deleteByUserIdAndDateCourseId(user.getId(), dateCourse.getId());
  }

  @Transactional(readOnly = true)
  public Page<RecentDateCourseDto> findMyCourse(String userEmail, Pageable pageable) {

    User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
        new UserNotFoundException("그런 이메일로 가입한 사용자는 없습니다."));

    Page<DateCourse> allDateCourseByUserId = dateCourseRepository
        .findAllDateCourseByUserId(user.getId(), pageable);
    return allDateCourseByUserId.map(RecentDateCourseDto::new);
  }

  @Transactional(readOnly = true)
  public Page<SavedDateCourseDto> findSavedCourseList(String userEmail, Pageable pageable) {

    User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
        new UserNotFoundException("그런 이메일로 가입한 사용자는 없습니다."));

    Page<UserDateCourseSave> allSavedCourseByUserId = userDateCourseSaveRepository
        .findAllUserDateCourseSaveByUserId(user.getId(), pageable);
    return allSavedCourseByUserId.map(SavedDateCourseDto::new);
  }

  @Transactional
  public void deleteCourseMadeByUser(Long courseId, String userEmail) {

    User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
        new UserNotFoundException("그런 이메일로 가입한 사용자는 없습니다."));
    DateCourse dateCourse = dateCourseRepository.findById(courseId).orElseThrow(() ->
        new DateCourseNotFoundException("찾으시는 데이트 코스는 없습니다."));

    dateCourseRepository.delete(dateCourse);
  }

  @Transactional
  public void addFollowerInUserFollowers(AddFollower addFollower, String userEmail) {

    User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
        new UserNotFoundException("그런 이메일로 가입한 사용자는 없습니다."));
    User targetUser = userRepository.findById(addFollower.getFollowerId()).orElseThrow(() ->
        new UserNotFoundException("존재하지 않는 사용자 입니다."));
    Follow follow = new Follow(addFollower.getFollowerId());
    UserFollow userFollow = new UserFollow(user, follow);

    followRepository.save(follow);
    userFollowRepository.save(userFollow);
  }

  public UserFollowingUsers getUserFollowingUsers(
      String userEmail) {
    User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
        new UserNotFoundException("그런 이메일로 가입한 사용자는 없습니다."));

    List<User> allFollowingUsers = userRepository.findAllFollowingUsers(userEmail);
    List<FollowingUserDto> followingUserDtos = allFollowingUsers.stream()
        .map(u -> new FollowingUserDto(u.getId(), u.getName(), u.getEmail())).collect(
            Collectors.toList());
    return new UserFollowingUsers(followingUserDtos);
  }
}
