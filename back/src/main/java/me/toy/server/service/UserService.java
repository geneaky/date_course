package me.toy.server.service;

import lombok.RequiredArgsConstructor;
import me.toy.server.dto.DateCourseResponseDto.RecentDateCourseDto;
import me.toy.server.dto.UserResponseDto.SavedDateCourseDto;
import me.toy.server.dto.UserResponseDto.UserDto;
import me.toy.server.entity.DateCourse;
import me.toy.server.entity.UserDateCourseSave;
import me.toy.server.entity.User;
import me.toy.server.exception.datecourse.DateCourseNotFoundException;
import me.toy.server.exception.user.UserNotFoundException;
import me.toy.server.repository.DateCourseRepository;
import me.toy.server.repository.UserDateCourseSaveRepository;
import me.toy.server.repository.UserRepository;
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
        new UserNotFoundException("그런 이메일로 가입한 사용자는 없습니다.")
    );

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
        new UserNotFoundException("그런 이메일로 가입한 사용자는 없습니다.")
    );
    DateCourse dateCourse = dateCourseRepository.findById(courseId).orElseThrow(() ->
        new DateCourseNotFoundException("찾으시는 데이트 코스는 없습니다."));

    userDateCourseSaveRepository.deleteByUserIdAndDateCourseId(user.getId(), dateCourse.getId());
  }

  @Transactional(readOnly = true)
  public List<RecentDateCourseDto> findMyCourse(String userEmail) {

    User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
        new UserNotFoundException("그런 이메일로 가입한 사용자는 없습니다.")
    );

    return dateCourseRepository.findAllDateCourseByUserId(user.getId());
  }

  @Transactional(readOnly = true)
  public List<SavedDateCourseDto> findSavedCourseList(String userEmail) {

    User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
        new UserNotFoundException("그런 이메일로 가입한 사용자는 없습니다.")
    );

    return dateCourseRepository.findAllSavedCourseByUserId(user.getId());
  }

  @Transactional
  public void deleteCourseMadeByUser(Long courseId, String userEmail) {

    User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
        new UserNotFoundException("그런 이메일로 가입한 사용자는 없습니다.")
    );
    DateCourse dateCourse = dateCourseRepository.findById(courseId).orElseThrow(() ->
        new DateCourseNotFoundException("찾으시는 데이트 코스는 없습니다."));

    dateCourseRepository.delete(dateCourse);
  }
}
