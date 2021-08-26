package me.toy.server.service;

import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import me.toy.server.cloud.S3Uploader;
import me.toy.server.dto.DateCourseRequestDto.RegistDateCourseRequestDto;
import me.toy.server.dto.DateCourseRequestDto.RegistDateCourseRequestDtoList;
import me.toy.server.dto.DateCourseResponseDto.RecentDateCourseDto;
import me.toy.server.dto.DateCourseResponseDto.LikeOrderDateCourseDto;
import me.toy.server.entity.*;
import me.toy.server.exception.DateCourseNotFoundException;
import me.toy.server.exception.UserNotFoundException;
import me.toy.server.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

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
  public void regist(RegistDateCourseRequestDtoList requestDtoList, String title,
      String userEmail) {

    User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
        new UserNotFoundException("그런 이메일을 가진 사용자는 없습니다.")
    );
    DateCourse dateCourse = new DateCourse(user, title);
    List<Location> locationList = new ArrayList<>();
    List<Tag> tagList = new ArrayList<>();
    List<LocationTag> locationTagList = new ArrayList<>();

    for (RegistDateCourseRequestDto requestDto : requestDtoList.getLocationList()) {
      if (requestDto.getFile() == null) {
        registerLocationsInDateCourse(dateCourse, requestDto, "", locationList, tagList,
            locationTagList);
        continue;
      }
      String fileSaveName =
          LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")) + ".jpg";
      registerLocationsInDateCourse(dateCourse, requestDto, fileSaveName, locationList, tagList,
          locationTagList);
      s3Uploader.upload(requestDto.getFile(), fileSaveName);
    }

    dateCourseRepository.save(dateCourse);
    locationRepository.saveAll(locationList);
    tagRepository.saveAll(tagList);
    locationTagRepository.saveAll(locationTagList);
  }

  private void registerLocationsInDateCourse(DateCourse dateCourse,
      RegistDateCourseRequestDto requestDto, String fileSaveName, List<Location> locationList,
      List<Tag> tagList,
      List<LocationTag> locationTagList) {

    Location location = new Location(requestDto, fileSaveName);
    location.setDateCourse(dateCourse);
    tagFindCircuit(requestDto, location, tagList, locationTagList);
    locationList.add(location);
  }

  private void tagFindCircuit(RegistDateCourseRequestDto requestDto, Location location,
      List<Tag> tagList, List<LocationTag> locationTagList) {

    for (String hashTag : requestDto.getHashTag()) {
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
  public void like(Long dateCourseId, String userEmail) {

    User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
        new UserNotFoundException("해당 이메일을 가진 사용자는 없습니다.")
    );
    DateCourse dateCourse = dateCourseRepository.findById(dateCourseId).orElseThrow(() ->
        new DateCourseNotFoundException("찾으시는 데이트 코스는 없습니다."));

    UserDateCourseLike userDateCourseLike = new UserDateCourseLike(user, dateCourse);
    userDateCourseLikeRepository.save(userDateCourseLike);
  }

  @Transactional
  public void unlike(Long dateCourseId, String userEmail) {

    User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
        new UserNotFoundException("해당 이메일을 가진 사용자는 없습니다.")
    );
    DateCourse dateCourse = dateCourseRepository.findById(dateCourseId).orElseThrow(() ->
        new DateCourseNotFoundException("찾으시는 데이트 코스는 없습니다."));
    userDateCourseLikeRepository.unlikeUserDateCourseLike(user.getId(), dateCourse.getId());
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
  public List<RecentDateCourseDto> getRecentDateCourseList() {

    return dateCourseRepository.findRecentDateCourse();
  }

  @Transactional(readOnly = true)
  public List<LikeOrderDateCourseDto> getLikedOrderDateCourseList() {

    return dateCourseRepository.findLikeOrderDateCourse();
  }
}
