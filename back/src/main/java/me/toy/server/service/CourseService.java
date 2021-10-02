package me.toy.server.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import me.toy.server.dto.course.CourseRequestDto.RegistCourseFormDto;
import me.toy.server.dto.course.CourseRequestDto.RegistLocationFormDto;
import me.toy.server.dto.course.CourseResponseDto.CourseDto;
import me.toy.server.dto.user.UserResponseDto.SavedCourseDto;
import me.toy.server.entity.Course;
import me.toy.server.entity.Location;
import me.toy.server.entity.LocationTag;
import me.toy.server.entity.Tag;
import me.toy.server.entity.User;
import me.toy.server.entity.UserCourseLike;
import me.toy.server.entity.UserCourseSave;
import me.toy.server.exception.course.AlreadyLikeCourseException;
import me.toy.server.exception.course.AlreadyUnlikeCourseException;
import me.toy.server.exception.course.CourseNotFoundException;
import me.toy.server.exception.user.UserNotFoundException;
import me.toy.server.repository.CourseRepository;
import me.toy.server.repository.LocationRepository;
import me.toy.server.repository.LocationTagRepository;
import me.toy.server.repository.TagRepository;
import me.toy.server.repository.UserCourseLikeRepository;
import me.toy.server.repository.UserCourseSaveRepository;
import me.toy.server.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CourseService {

  private final UserRepository userRepository;
  private final CourseRepository courseRepository;
  private final LocationRepository locationRepository;
  private final TagRepository tagRepository;
  private final LocationTagRepository locationTagRepository;
  private final UserCourseLikeRepository userCourseLikeRepository;
  private final UserCourseSaveRepository userCourseSaveRepository;
  private final FileService s3Service;

  @Transactional
  public void registCourse(RegistCourseFormDto registCourseFormDto, String userEmail) {

    User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
        new UserNotFoundException("그런 이메일을 가진 사용자는 없습니다.")
    );
    Course course = new Course(user, registCourseFormDto.getCourseTitle());
    List<Location> locationList = new ArrayList<>();
    List<Tag> tagList = new ArrayList<>();
    List<LocationTag> locationTagList = new ArrayList<>();

    for (RegistLocationFormDto registLocationFormDto : registCourseFormDto.getLocationList()) {
      String fileSaveName = s3Service.upload(registLocationFormDto.getFile());
      addLocation(
          course,
          registLocationFormDto,
          fileSaveName,
          locationList,
          tagList,
          locationTagList);
    }

    courseRepository.save(course);
    locationRepository.saveAll(locationList);
    tagRepository.saveAll(tagList);
    locationTagRepository.saveAll(locationTagList);
  }

  private void addLocation(Course course,
      RegistLocationFormDto registLocationFormDto, String fileSaveName, List<Location> locationList,
      List<Tag> tagList,
      List<LocationTag> locationTagList) {

    Location location = new Location(registLocationFormDto, fileSaveName);
    location.setCourse(course);
    if (registLocationFormDto.getHashTag() != null) {
      tagFindCircuit(registLocationFormDto, location, tagList, locationTagList);
    }
    locationList.add(location);
  }

  private void tagFindCircuit(RegistLocationFormDto registLocationFormDto, Location location,
      List<Tag> tagList, List<LocationTag> locationTagList) {

    for (String hashTag : registLocationFormDto.getHashTag()) {
      Optional<Tag> opTag = tagRepository.findByName(hashTag);
      if (opTag.isPresent()) {
        LocationTag locationTag = new LocationTag(location, opTag.get());
        locationTagList.add(locationTag);
        continue;
      }
      Tag tag = new Tag(hashTag);
      tagList.add(tag);
      LocationTag locationTag = new LocationTag(location, tag);
      locationTagList.add(locationTag);
    }
  }

  @Transactional
  public void likeCourse(Long dateCourseId, String userEmail) {

    User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
        new UserNotFoundException("해당 이메일을 가진 사용자는 없습니다.")
    );
    Course course = courseRepository.findById(dateCourseId).orElseThrow(() ->
        new CourseNotFoundException("찾으시는 데이트 코스는 없습니다."));

    if (userCourseLikeRepository.findLikeByUserIdAndCourseId(user.getId(),
        course.getId()).isPresent()) {
      throw new AlreadyLikeCourseException("이미 좋아요 표시된 코스입니다.");
    }

    UserCourseLike userCourseLike = new UserCourseLike(user, course);
    userCourseLikeRepository.save(userCourseLike);
  }

  @Transactional
  public void unlikeCourse(Long dateCourseId, String userEmail) {

    User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
        new UserNotFoundException("해당 이메일을 가진 사용자는 없습니다.")
    );
    Course course = courseRepository.findById(dateCourseId).orElseThrow(() ->
        new CourseNotFoundException("찾으시는 데이트 코스는 없습니다."));

    if (!userCourseLikeRepository.findLikeByUserIdAndCourseId(user.getId(),
        course.getId()).isPresent()) {
      throw new AlreadyUnlikeCourseException("좋아요 표시되지 않은 코스는 좋아요 취소가 불가능합니다.");
    }

    userCourseLikeRepository.unlikeCourse(user.getId(), course.getId());
  }

  @Transactional(readOnly = true)
  public Page<CourseDto> getRecentCourses(Pageable pageable) {

    Page<Course> coursePage = courseRepository.findAll(pageable);

    return coursePage.map(CourseDto::new);
  }

  @Transactional(readOnly = true)
  public Page<CourseDto> getLikedOrderCourses(Pageable pageable) {

    return courseRepository.findLikeOrderCourse(pageable);
  }

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
  public void removeCourse(Long courseId, String userEmail) {

    User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
        new UserNotFoundException("그런 이메일로 가입한 사용자는 없습니다."));
    Course course = courseRepository.findById(courseId).orElseThrow(() ->
        new CourseNotFoundException("찾으시는 데이트 코스는 없습니다."));

    userCourseSaveRepository.deleteByUserIdAndCourseId(user.getId(), course.getId());
  }

  @Transactional(readOnly = true)
  public Page<SavedCourseDto> getSavedCourses(String userEmail, Pageable pageable) {

    User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
        new UserNotFoundException("그런 이메일로 가입한 사용자는 없습니다."));

    Page<UserCourseSave> allSavedCourseByUserId = userCourseSaveRepository
        .findAllUserCourseSavePageByUserId(user.getId(), pageable);
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
  public Page<CourseDto> getMyCourses(String userEmail, Pageable pageable) {

    User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
        new UserNotFoundException("그런 이메일로 가입한 사용자는 없습니다."));

    Page<Course> coursePage = courseRepository
        .findAllCourseByUserId(user.getId(), pageable);
    return coursePage.map(CourseDto::new);
  }

  @Transactional
  public void removeMyCourse(Long courseId, String userEmail) {

    userRepository.findByEmail(userEmail).orElseThrow(() ->
        new UserNotFoundException("그런 이메일로 가입한 사용자는 없습니다."));
    Course course = courseRepository.findById(courseId).orElseThrow(() ->
        new CourseNotFoundException("찾으시는 데이트 코스는 없습니다."));

    courseRepository.delete(course);
  }
}
