package me.toy.server.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import me.toy.server.dto.course.CourseResponseDto.CourseDto;
import me.toy.server.dto.user.UserResponseDto.SavedCourseDto;
import me.toy.server.entity.Course;
import me.toy.server.entity.User;
import me.toy.server.entity.UserCourseSave;
import me.toy.server.exception.course.CourseNotFoundException;
import me.toy.server.exception.user.UserNotFoundException;
import me.toy.server.repository.CourseRepository;
import me.toy.server.repository.UserCourseSaveRepository;
import me.toy.server.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MyCourseService {

  private final UserRepository userRepository;
  private final CourseRepository courseRepository;
  private final UserCourseSaveRepository userCourseSaveRepository;

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

  @Transactional
  public void removeCourse(Long courseId, Long userId) {

    Course course = courseRepository.findById(courseId).orElseThrow(() ->
        new CourseNotFoundException("찾으시는 데이트 코스는 없습니다."));

    userCourseSaveRepository.deleteByUserIdAndCourseId(userId, course.getId());
  }

  @Transactional(readOnly = true)
  public Page<SavedCourseDto> getSavedCourses(Long userId, Pageable pageable) {

    Page<UserCourseSave> allSavedCourseByUserId = userCourseSaveRepository
        .findAllUserCourseSavePageByUserId(userId, pageable);
    return allSavedCourseByUserId.map(SavedCourseDto::new);
  }

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

  @Transactional(readOnly = true)
  public Page<CourseDto> getMyCourses(Long userId, Pageable pageable) {

    Page<Course> coursePage = courseRepository
        .findAllCourseByUserId(userId, pageable);
    return coursePage.map(CourseDto::new);
  }

  @Transactional
  public void removeMyCourse(Long courseId, Long userId) {

    Course course = courseRepository.findByIdAndUserId(courseId, userId).orElseThrow(() ->
        new CourseNotFoundException("찾으시는 데이트 코스는 없습니다."));

    courseRepository.delete(course);
  }
}
