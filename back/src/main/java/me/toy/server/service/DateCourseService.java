package me.toy.server.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import me.toy.server.cloud.S3Uploader;
import me.toy.server.dto.DateCourseRequestDto.RegistDateCourseFormDto;
import me.toy.server.dto.DateCourseRequestDto.RegistLocationFormDto;
import me.toy.server.dto.DateCourseResponseDto.LikeOrderDateCourseDto;
import me.toy.server.dto.DateCourseResponseDto.RecentDateCourseDto;
import me.toy.server.entity.Comment;
import me.toy.server.entity.DateCourse;
import me.toy.server.entity.Location;
import me.toy.server.entity.LocationTag;
import me.toy.server.entity.Tag;
import me.toy.server.entity.User;
import me.toy.server.entity.UserDateCourseLike;
import me.toy.server.exception.datecourse.AlreadyLikeCourseException;
import me.toy.server.exception.datecourse.AlreadyUnlikeCourseException;
import me.toy.server.exception.datecourse.DateCourseNotFoundException;
import me.toy.server.exception.user.UserNotFoundException;
import me.toy.server.repository.CommentRepository;
import me.toy.server.repository.DateCourseRepository;
import me.toy.server.repository.LocationRepository;
import me.toy.server.repository.LocationTagRepository;
import me.toy.server.repository.TagRepository;
import me.toy.server.repository.UserDateCourseLikeRepository;
import me.toy.server.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DateCourseService {

  private final UserRepository userRepository;
  private final DateCourseRepository dateCourseRepository;
  private final LocationRepository locationRepository;
  private final LocationTagRepository locationTagRepository;
  private final CommentRepository commentRepository;
  private final UserDateCourseLikeRepository userDateCourseLikeRepository;
  private final TagRepository tagRepository;
  private final S3Uploader s3Uploader;

  @Transactional
  public void registDateCourse(RegistDateCourseFormDto registDateCourseFormDto, String userEmail) {

    User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
        new UserNotFoundException("그런 이메일을 가진 사용자는 없습니다.")
    );
    DateCourse dateCourse = new DateCourse(user, registDateCourseFormDto.getCourseTitle());
    List<Location> locationList = new ArrayList<>();
    List<Tag> tagList = new ArrayList<>();
    List<LocationTag> locationTagList = new ArrayList<>();

    for (RegistLocationFormDto registLocationFormDto : registDateCourseFormDto.getLocationList()) {
      String fileSaveName = s3Uploader.upload(registLocationFormDto.getFile());
      addLocationInDateCourse(
          dateCourse,
          registLocationFormDto,
          fileSaveName,
          locationList,
          tagList,
          locationTagList);
    }

    dateCourseRepository.save(dateCourse);
    locationRepository.saveAll(locationList);
    tagRepository.saveAll(tagList);
    locationTagRepository.saveAll(locationTagList);
  }

  private void addLocationInDateCourse(DateCourse dateCourse,
      RegistLocationFormDto registLocationFormDto, String fileSaveName, List<Location> locationList,
      List<Tag> tagList,
      List<LocationTag> locationTagList) {

    Location location = new Location(registLocationFormDto, fileSaveName);
    location.setDateCourse(dateCourse);
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
  public void likeDateCourse(Long dateCourseId, String userEmail) {

    User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
        new UserNotFoundException("해당 이메일을 가진 사용자는 없습니다.")
    );
    DateCourse dateCourse = dateCourseRepository.findById(dateCourseId).orElseThrow(() ->
        new DateCourseNotFoundException("찾으시는 데이트 코스는 없습니다."));

    if (userDateCourseLikeRepository.findLikeByUserIdAndDateCourseId(user.getId(),
        dateCourse.getId()).isPresent()) {
      throw new AlreadyLikeCourseException("이미 좋아요 표시된 코스입니다.");
    }

    UserDateCourseLike userDateCourseLike = new UserDateCourseLike(user, dateCourse);
    userDateCourseLikeRepository.save(userDateCourseLike);
  }

  @Transactional
  public void unlikeDateCourse(Long dateCourseId, String userEmail) {

    User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
        new UserNotFoundException("해당 이메일을 가진 사용자는 없습니다.")
    );
    DateCourse dateCourse = dateCourseRepository.findById(dateCourseId).orElseThrow(() ->
        new DateCourseNotFoundException("찾으시는 데이트 코스는 없습니다."));

    if (!userDateCourseLikeRepository.findLikeByUserIdAndDateCourseId(user.getId(),
        dateCourse.getId()).isPresent()) {
      throw new AlreadyUnlikeCourseException("좋아요 표시되지 않은 코스는 좋아요 취소가 불가능합니다.");
    }

    userDateCourseLikeRepository.unlikeDateCourse(user.getId(), dateCourse.getId());
  }

  @Transactional
  public void registComment(Long courseId, String comment, String userEmail) {

    User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
        new UserNotFoundException("해당 이메일을 가진 사용자는 없습니다.")
    );
    DateCourse dateCourse = dateCourseRepository.findById(courseId).orElseThrow(() ->
        new DateCourseNotFoundException("찾으시는 데이트 코스는 없습니다."));

    Comment dateCourseComment = new Comment(user, dateCourse, comment);
    commentRepository.save(dateCourseComment);
  }

  @Transactional(readOnly = true)
  public Page<RecentDateCourseDto> getRecentDateCourses(Pageable pageable) {

    Page<DateCourse> allDateCourse = dateCourseRepository.findAll(pageable);

    return allDateCourse.map(RecentDateCourseDto::new);
  }

  @Transactional(readOnly = true)
  public Page<LikeOrderDateCourseDto> getLikedOrderDateCourses(Pageable pageable) {

    return dateCourseRepository.findLikeOrderDateCourse(pageable);
  }

}
