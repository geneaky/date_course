package me.toy.server.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import me.toy.server.dto.course.CourseRequestDto.RegistCourseFormDto;
import me.toy.server.dto.course.CourseRequestDto.RegistLocationFormDto;
import me.toy.server.dto.course.CourseResponseDto.LikeOrderCourseDto;
import me.toy.server.dto.course.CourseResponseDto.RecentCourseDto;
import me.toy.server.dto.user.UserResponseDto.SavedCourseDto;
import me.toy.server.entity.Course;
import me.toy.server.entity.Tag;
import me.toy.server.entity.User;
import me.toy.server.entity.UserCourseLike;
import me.toy.server.entity.UserCourseSave;
import me.toy.server.exception.course.AlreadyLikeCourseException;
import me.toy.server.exception.course.AlreadyUnlikeCourseException;
import me.toy.server.exception.course.CourseNotFoundException;
import me.toy.server.exception.user.UserNotFoundException;
import me.toy.server.repository.CommentRepository;
import me.toy.server.repository.CourseRepository;
import me.toy.server.repository.LocationRepository;
import me.toy.server.repository.LocationTagRepository;
import me.toy.server.repository.TagRepository;
import me.toy.server.repository.UserCourseLikeRepository;
import me.toy.server.repository.UserCourseSaveRepository;
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
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

  @Mock
  UserRepository userRepository;
  @Mock
  CourseRepository courseRepository;
  @Mock
  LocationRepository locationRepository;
  @Mock
  UserCourseLikeRepository userCourseLikeRepository;
  @Mock
  UserCourseSaveRepository userCourseSaveRepository;
  @Mock
  CommentRepository commentRepository;
  @Mock
  TagRepository tagRepository;
  @Mock
  LocationTagRepository locationTagRepository;
  @Mock
  FileService s3Service;
  @InjectMocks
  CourseService courseService;

  private User createUser() {

    return User.builder()
        .email("test@naver.com")
        .course(new ArrayList<>())
        .userCourseLikes(new ArrayList<>())
        .userCourseSaves(new ArrayList<>())
        .comments(new ArrayList<>())
        .build();
  }

  private ArrayList<RegistLocationFormDto> createRequestList() {

    ArrayList<RegistLocationFormDto> list = new ArrayList<>();

    return list;
  }

  @Test
  @DisplayName("데이트 코스 등록 성공")
  public void registCourseTest() throws Exception {

    String userEmail = "test@naver.com";
    String title = "testTitle";
    User user = createUser();
    RegistCourseFormDto registCourseFormDto = mock(RegistCourseFormDto.class);
    RegistLocationFormDto requestDto = mock(RegistLocationFormDto.class);
    ArrayList<RegistLocationFormDto> list = createRequestList();
    list.add(requestDto);

    when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
    when(registCourseFormDto.getLocationList()).thenReturn(list);

    courseService.registCourse(registCourseFormDto, userEmail);

    verify(userRepository, atLeastOnce()).findByEmail(userEmail);
    verify(courseRepository, times(1)).save(any());
    verify(locationRepository, times(1)).saveAll(any());
    verify(tagRepository, times(1)).saveAll(any());
    verify(locationTagRepository, times(1)).saveAll(any());
  }

  @Test
  @DisplayName("인가 받지 않은 사용자의 코스 등록은 예외가 발생 합니다.")
  public void registCourseWithUnAuthorizedUser() throws Exception {

    RegistCourseFormDto registCourseFormDto = mock(RegistCourseFormDto.class);
    String unAuthorizedUserEmail = "NONONO@gmail.com";

    assertThrows(UserNotFoundException.class, () -> {
      courseService.registCourse(registCourseFormDto, unAuthorizedUserEmail);
    });

  }

  @Test
  @DisplayName("사진 첨부 없는 데이트 코스 등록 성공")
  public void registCourseWithoutPhotosTest() throws Exception {

    String userEmail = "test@naver.com";
    String title = "testTitle";
    User user = createUser();
    RegistCourseFormDto registCourseFormDto = mock(RegistCourseFormDto.class);
    RegistLocationFormDto requestDto = mock(RegistLocationFormDto.class);
    ArrayList<RegistLocationFormDto> list = createRequestList();
    list.add(requestDto);

    when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
    when(registCourseFormDto.getLocationList()).thenReturn(list);
    when(requestDto.getFile()).thenReturn(null);

    courseService.registCourse(registCourseFormDto, userEmail);

    verify(courseRepository, atMostOnce()).save(any());
    verify(locationRepository, times(1)).saveAll(any());
    verify(tagRepository, times(1)).saveAll(any());
    verify(locationTagRepository, times(1)).saveAll(any());
    verify(s3Service, times(1)).upload(any());
  }

  @Test
  @DisplayName("사진 첨부된 데이트 코스 등록 성공")
  public void registCourseWithPhotosTest() throws Exception {

    String userEmail = "test@naver.com";
    String title = "testTitle";
    User user = createUser();
    RegistCourseFormDto registCourseFormDto = mock(RegistCourseFormDto.class);
    RegistLocationFormDto requestDto = mock(RegistLocationFormDto.class);
    ArrayList<RegistLocationFormDto> list = createRequestList();
    list.add(requestDto);

    when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
    when(registCourseFormDto.getLocationList()).thenReturn(list);
    when(requestDto.getFile()).thenReturn(mock(MultipartFile.class));

    courseService.registCourse(registCourseFormDto, userEmail);

    verify(courseRepository, atMostOnce()).save(any());
    verify(locationRepository, times(1)).saveAll(any());
    verify(tagRepository, times(1)).saveAll(any());
    verify(locationTagRepository, times(1)).saveAll(any());
    verify(s3Service, times(1)).upload(any());
  }

  @Test
  @DisplayName("사진 첨부가 일부만 되어있는 데이트 코스 등록 성공")
  public void registCourseWithPartialPhotosTest() throws Exception {

    String userEmail = "test@naver.com";
    String title = "testTitle";
    User user = createUser();
    RegistCourseFormDto registCourseFormDto = mock(RegistCourseFormDto.class);
    RegistLocationFormDto requestDto1 = mock(RegistLocationFormDto.class);
    RegistLocationFormDto requestDto2 = mock(RegistLocationFormDto.class);
    RegistLocationFormDto requestDto3 = mock(RegistLocationFormDto.class);
    ArrayList<RegistLocationFormDto> list = createRequestList();
    list.add(requestDto1);
    list.add(requestDto2);
    list.add(requestDto3);

    when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
    when(registCourseFormDto.getLocationList()).thenReturn(list);
    when(requestDto1.getFile()).thenReturn(null);
    when(requestDto2.getFile()).thenReturn(mock(MultipartFile.class));
    when(requestDto3.getFile()).thenReturn(mock(MultipartFile.class));

    courseService.registCourse(registCourseFormDto, userEmail);

    verify(courseRepository, atMostOnce()).save(any());
    verify(locationRepository, times(1)).saveAll(any());
    verify(tagRepository, times(1)).saveAll(any());
    verify(locationTagRepository, times(1)).saveAll(any());
    verify(s3Service, times(3)).upload(any());
  }

  @Test
  @DisplayName("태그가 걸려있는 데이트 코스 태그 등록 성공")
  public void registCourseWithExistedTagsTest() throws Exception {

    String title = "testTitle";
    String userEmail = "test@naver.com";
    User user = createUser();
    RegistCourseFormDto registCourseFormDto = mock(RegistCourseFormDto.class);
    RegistLocationFormDto requestDto = mock(RegistLocationFormDto.class);
    ArrayList<RegistLocationFormDto> list = createRequestList();
    list.add(requestDto);
    List<String> hashTag = new ArrayList<>(Arrays.asList("#hi", "#bi", "#gg"));
    Tag tag1 = new Tag("#hi");
    Tag tag2 = new Tag("#bi");
    Tag tag3 = new Tag("#gg");

    when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
    when(registCourseFormDto.getLocationList()).thenReturn(list);
    when(requestDto.getHashTag()).thenReturn(hashTag);
    when(tagRepository.findByName("#hi")).thenReturn(Optional.of(tag1));
    when(tagRepository.findByName("#bi")).thenReturn(Optional.of(tag2));
    when(tagRepository.findByName("#gg")).thenReturn(Optional.of(tag3));

    courseService.registCourse(registCourseFormDto, userEmail);

    verify(courseRepository, atMostOnce()).save(any());
    verify(locationRepository, times(1)).saveAll(any());
    verify(tagRepository, times(1)).saveAll(any());
    verify(locationTagRepository, times(1)).saveAll(any());
    verify(s3Service, times(1)).upload(any());
  }

  @Test
  @DisplayName("새로운 데이트 코스 태그 등록")
  public void registCourseWithNewTagsTest() throws Exception {

    String title = "testTitle";
    String userEmail = "test@naver.com";
    User user = createUser();
    RegistCourseFormDto registCourseFormDto = mock(RegistCourseFormDto.class);
    RegistLocationFormDto requestDto = mock(RegistLocationFormDto.class);
    ArrayList<RegistLocationFormDto> list = createRequestList();
    list.add(requestDto);
    List<String> hashTag = new ArrayList<>(Arrays.asList("#hi", "#bi", "#gg"));
    Tag tag1 = new Tag("#hi");
    Tag tag2 = new Tag("#bi");

    when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
    when(registCourseFormDto.getLocationList()).thenReturn(list);
    when(requestDto.getHashTag()).thenReturn(hashTag);
    when(tagRepository.findByName("#hi")).thenReturn(Optional.of(tag1));
    when(tagRepository.findByName("#bi")).thenReturn(Optional.of(tag2));

    courseService.registCourse(registCourseFormDto, userEmail);

    verify(courseRepository, atMostOnce()).save(any());
    verify(locationRepository, times(1)).saveAll(any());
    verify(tagRepository, times(1)).saveAll(any());
    verify(locationTagRepository, times(1)).saveAll(any());
    verify(s3Service, times(1)).upload(any());
  }

  @Test
  @DisplayName("좋아요를 누르지 않은 코스라면 좋아요를 등록한다.")
  public void likeCourseTest() throws Exception {

    String title = "testTitle";
    String userEmail = "test@naver.com";
    User user = createUser();
    Course course = new Course(user, title);

    when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
    when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));
    when(userCourseLikeRepository.findLikeByUserIdAndCourseId(user.getId(),
        course.getId())).thenReturn(Optional.empty());

    courseService.likeCourse(course.getId(), userEmail);

    verify(userCourseLikeRepository, times(1)).save(any());
  }

  @Test
  @DisplayName("인가 받지 않은 사용자의 좋아요는 예외가 발생 합니다.")
  public void likeCourseWithUnAuthorizedUserTest() throws Exception {

    Long courseId = 1L;
    String unAuthorizedUserEmail = "NONONONO@gamil.com";

    assertThrows(UserNotFoundException.class, () -> {
      courseService.likeCourse(courseId, unAuthorizedUserEmail);
    });
  }

  @Test
  @DisplayName("존재하지 않는 코스에 대한 좋아요는 예외가 발생 합니다.")
  public void likeNotExistedCourseTest() throws Exception {

    Long courseId = 1L;
    String unAuthorizedUserEmail = "NONONONO@gamil.com";
    User mockedUser = mock(User.class);
    when(userRepository.findByEmail(unAuthorizedUserEmail)).thenReturn(
        Optional.ofNullable(mockedUser));

    assertThrows(CourseNotFoundException.class, () -> {
      courseService.likeCourse(courseId, unAuthorizedUserEmail);
    });

  }

  @Test
  @DisplayName("이미 좋아요 등록된 코스에 대한 좋아요는 예외를 발생시킵니다.")
  public void likeAlreadyLikedCourseTest() throws Exception {

    User mockedUser = mock(User.class);
    String mockedUserEmail = "nonono@gmail.com";
    Course mockedCourse = mock(Course.class);
    Long mockedCourseId = 1L;
    UserCourseLike userCourseLike = mock(UserCourseLike.class);
    when(userRepository.findByEmail(any())).thenReturn(Optional.ofNullable(mockedUser));
    when(courseRepository.findById(any())).thenReturn(Optional.ofNullable(mockedCourse));
    when(userCourseLikeRepository.findLikeByUserIdAndCourseId(any(), any())).thenReturn(
        Optional.of(userCourseLike));

    assertThrows(AlreadyLikeCourseException.class, () -> {
      courseService.likeCourse(mockedCourseId, mockedUserEmail);
    });
  }

  @Test
  @DisplayName("좋아요를 누른 코스라면 좋아요를 취소한다")
  public void unlikeCourseTest() throws Exception {
    String title = "testTitle";
    String userEmail = "test@naver.com";
    User user = createUser();
    Course course = new Course(user, title);
    UserCourseLike userCourseLike = mock(UserCourseLike.class);
    when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
    when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));
    when(userCourseLikeRepository.findLikeByUserIdAndCourseId(user.getId(),
        course.getId())).thenReturn(Optional.of(userCourseLike));

    courseService.unlikeCourse(course.getId(), userEmail);

    verify(userCourseLikeRepository, times(1)).unlikeCourse(user.getId(),
        course.getId());
  }

  @Test
  @DisplayName("인가 받지 않은 사용자의 좋아요 취소는 예외가 발생한다.")
  public void unlikeCourseWithUnAuthorizedUserTest() throws Exception {

    Long courseId = 1L;
    String unAuthorizedUserEmail = "NONONONO@gamil.com";

    assertThrows(UserNotFoundException.class, () -> {

      courseService.unlikeCourse(courseId, unAuthorizedUserEmail);
    });
  }

  @Test
  @DisplayName("존재하지 않는 코스에 대한 좋아요 취소는 예외가 발생한다.")
  public void unlikeNotExistedCourseTest() throws Exception {

    String mockedUserEmail = "OKOKOK@gmail.com";
    Long mockedCourseId = 1L;
    User user = mock(User.class);

    when(userRepository.findByEmail(mockedUserEmail)).thenReturn(Optional.ofNullable(user));
    when(courseRepository.findById(mockedCourseId)).thenReturn(Optional.empty());

    assertThrows(CourseNotFoundException.class, () -> {
      courseService.unlikeCourse(mockedCourseId, mockedUserEmail);
    });
  }

  @Test
  @DisplayName("좋아요를 누르지 않은 코스를 좋아요 취소시 예외가 발생합니다.")
  public void unlikeCousreAlreadyUnlikedTest() throws Exception {

    User mockedUser = mock(User.class);
    String mockedUserEmail = "OKOKOK@gmail.com";
    Course mockedCourse = mock(Course.class);
    Long mockedCourseId = 1L;

    when(userRepository.findByEmail(any())).thenReturn(Optional.ofNullable(mockedUser));
    when(courseRepository.findById(any())).thenReturn(Optional.ofNullable(mockedCourse));
    when(userCourseLikeRepository.findLikeByUserIdAndCourseId(any(), any())).thenReturn(
        Optional.empty());

    assertThrows(AlreadyUnlikeCourseException.class, () -> {
      courseService.unlikeCourse(mockedCourseId, mockedUserEmail);
    });
  }

  @Test
  @DisplayName("최신순 코스를 페이징하여 반환 합니다.")
  public void getRecentCoursesTest() throws Exception {

    User user = User.builder()
        .email("test@naver.com")
        .course(new ArrayList<>())
        .userCourseLikes(new ArrayList<>())
        .userCourseSaves(new ArrayList<>())
        .build();
    Course course1 = new Course(user, "course1");
    Course course2 = new Course(user, "course2");
    Course course3 = new Course(user, "course3");
    List<Course> courses = new ArrayList<>(Arrays.asList(course3, course2, course1));

    Pageable pageable = PageRequest.of(1, 3, Direction.DESC, "recent");
    Page<Course> coursePage = new PageImpl<>(courses, pageable, 3);
    Page<RecentCourseDto> result = coursePage.map(RecentCourseDto::new);
    when(courseRepository.findAll(pageable)).thenReturn(coursePage);

    Page<RecentCourseDto> recentCourses = courseService.getRecentCourses(pageable);

    assertThat(recentCourses.getContent()).size().isEqualTo(3);
    assertThat(recentCourses.getContent()).usingRecursiveComparison().isEqualTo(result);
    verify(courseRepository).findAll(pageable);
  }

  @Test
  @DisplayName("좋아요순 코스를 페이징하여 반환합니다.")
  public void getLikeOrderCoursesTest() throws Exception {

    Pageable pageable = PageRequest.of(1, 3, Direction.DESC, "likeCount");
    User user = User.builder()
        .email("test@naver.com")
        .course(new ArrayList<>())
        .userCourseLikes(new ArrayList<>())
        .userCourseSaves(new ArrayList<>())
        .build();
    Course course1 = new Course(user, "course1");
    Course course2 = new Course(user, "course2");
    Course course3 = new Course(user, "course3");
    LikeOrderCourseDto courseDto1 = new LikeOrderCourseDto(1L, 3, course1.getLocations());
    LikeOrderCourseDto courseDto2 = new LikeOrderCourseDto(2L, 2, course2.getLocations());
    LikeOrderCourseDto courseDto3 = new LikeOrderCourseDto(3L, 1, course3.getLocations());
    List<LikeOrderCourseDto> courses = new ArrayList<>(
        Arrays.asList(courseDto1, courseDto2, courseDto3));

    Page<LikeOrderCourseDto> page = new PageImpl<>(courses, pageable, 3);

    when(courseRepository.findLikeOrderCourse(pageable)).thenReturn(page);

    Page<LikeOrderCourseDto> likedOrderCourses = courseService.getLikedOrderCourses(pageable);

    assertThat(likedOrderCourses.getContent()).size().isEqualTo(3);
    assertThat(likedOrderCourses.getContent()).usingRecursiveComparison().isEqualTo(page);
    verify(courseRepository).findLikeOrderCourse((pageable));
  }

  @Test
  @DisplayName("사용자가 좋아요 누른 코스의 ID 목록을 반환한다")
  public void getUserLikedDateCourseIDs() throws Exception {

    User user = User.builder()
        .email("test@naver.com")
        .course(new ArrayList<>())
        .userCourseLikes(new ArrayList<>())
        .build();
    Course course = new Course(user, "test");
    UserCourseLike userCourseLike = new UserCourseLike(user, course);
    userRepository.save(user);

    when(userRepository.findByEmail("test@naver.com"))
        .thenReturn(Optional.of(user));

    List<Long> testLikedCourse = user.getUserCourseLikes()
        .stream()
        .map(testLike -> testLike.getCourse().getId())
        .collect(Collectors.toList());

    List<Long> likedCourse = courseService.getLikedCourseIds("test@naver.com");

    assertEquals(testLikedCourse, likedCourse);
    verify(userRepository, atLeast(1)).findByEmail(any());
  }

  @Test
  @DisplayName("인가 받지 않은 사용자가 좋아요를 등록한 코스의 ID를 요청시 예외가 발생한다.")
  public void getLikedCourseIdsWithUnAuthorizedUser() throws Exception {

    when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> {
      courseService.getLikedCourseIds(any());
    });
  }

  @Test
  @DisplayName("사용자가 좋아요 누른 코스가 없을시 빈 목록을 반환한다")
  public void whenNoUserLikedDateCourseThenReturnEmptyList() throws Exception {

    User user = User.builder()
        .email("test@naver.com")
        .userCourseLikes(new ArrayList<>())
        .build();
    userRepository.save(user);

    when(userRepository.findByEmail("test@naver.com"))
        .thenReturn(Optional.of(user));
    List<Long> likedCourse = courseService.getLikedCourseIds("test@naver.com");

    assertEquals(user.getUserCourseLikes(), likedCourse);
    verify(userRepository, atLeast(1)).findByEmail(any());
  }

  @Test
  @DisplayName("사용자가 저장할 코스를 등록한다.")
  public void registDateCourseUserWantToSave() throws Exception {

    User user = User.builder()
        .email("test@naver.com")
        .course(new ArrayList<>())
        .userCourseSaves(new ArrayList<>())
        .build();
    Course course = new Course(user, "test");
    UserCourseSave userCourseSave = new UserCourseSave(user, course);

    when(userRepository.findByEmail("test@naver.com")).thenReturn(Optional.of(user));
    when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));

    courseService.addCourse(course.getId(), "test@naver.com");

    verify(userRepository).findByEmail("test@naver.com");
    verify(courseRepository).findById(any());
    verify(userCourseSaveRepository).save(any());
  }

  @Test
  @DisplayName("인가 받지 않은 사용자가 코스 등록시 예외가 발생한다.")
  public void addCourseWithUnAuthorizedUserTest() throws Exception {

    when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> {
      courseService.addCourse(any(), "NONO@naver.com");
    });
  }

  @Test
  @DisplayName("존재하지 않는 코스를 등록하려고 하면 예외가 발생한다.")
  public void addNotExistedCourseTest() throws Exception {

    User mockedUser = mock(User.class);

    when(userRepository.findByEmail(any())).thenReturn(Optional.ofNullable(mockedUser));
    when(courseRepository.findById(any())).thenReturn(Optional.empty());

    assertThrows(CourseNotFoundException.class, () -> {
      courseService.addCourse(1L, "OKOK@naver.com");
    });
  }


  @Test
  @DisplayName("사용자가 저장한 데이트 코스의 ID 목록을 반환한다")
  public void getUserSavedDateCourseIDs() throws Exception {

    User user = User.builder()
        .email("test@naver.com")
        .course(new ArrayList<>())
        .userCourseSaves(new ArrayList<>())
        .build();
    Course course = new Course(user, "test");
    UserCourseSave userCourseSave = new UserCourseSave(user, course);

    when(userRepository.findByEmail("test@naver.com")).thenReturn(Optional.of(user));
    List<Long> resultSavedCourse = courseService.getSavedCourseIds("test@naver.com");
    List<Long> testSavedCourseList = user.getUserCourseSaves()
        .stream()
        .map(testSavedCourse -> testSavedCourse.getCourse().getId())
        .collect(Collectors.toList());

    assertEquals(resultSavedCourse, testSavedCourseList);
    verify(userRepository, atLeast(1)).findByEmail(any());
  }

  @Test
  @DisplayName("인가 받지 않은 사용자가 저장된 코스 ID 요청시 예외가 발생한다.")
  public void getSavedCourseIdsWithUnAuthorizedUserTest() throws Exception {

    when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> {
      courseService.getSavedCourseIds("NONO@naver.com");
    });
  }

  @Test
  @DisplayName("사용자가 저장한 데이트 코스가 없을시 빈 목록을 반환한다")
  public void whenNoUserSavedDateCourseThenReturnEmptyList() throws Exception {

    User user = User.builder()
        .email("test@naver.com")
        .userCourseSaves(new ArrayList<>())
        .build();
    List<Long> testSavedCourse = new ArrayList<>();

    when(userRepository.findByEmail("test@naver.com")).thenReturn(Optional.of(user));
    List<Long> savedCourse = courseService.getSavedCourseIds("test@naver.com");

    assertEquals(savedCourse, testSavedCourse);
    verify(userRepository, atLeast(1)).findByEmail(any());
  }

  @Test
  @DisplayName("사용자가 지정한 데이트 코스를 저장한 데이트 코스에서 삭제한다")
  public void deleteDateCourseAtUserSavedDateCourses() throws Exception {

    User user = User.builder()
        .email("test@naver.com")
        .course(new ArrayList<>())
        .build();
    Course course = new Course(user, "test");

    when(userRepository.findByEmail("test@naver.com")).thenReturn(Optional.of(user));
    when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));
    courseService.removeCourse(course.getId(), "test@naver.com");

    verify(userRepository, atLeast(1)).findByEmail(any());
    verify(courseRepository, atLeast(1)).findById(any());
    verify(userCourseSaveRepository, times(1)).deleteByUserIdAndCourseId(any(), any());
  }

  @Test
  @DisplayName("인가 받지 않은 사용자가 코스를 지우려고 하면 예외가 발생한다.")
  public void removeCourseWithUnAuthorizedUser() throws Exception {

    when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> {
      courseService.removeCourse(any(), "NONO@naver.com");
    });
  }

  @Test
  @DisplayName("등록되지 않은 코스를 삭제 시도시 예외가 발생한다.")
  public void removeNotExistedCourseTest() throws Exception {

    User mockedUser = mock(User.class);

    when(userRepository.findByEmail(any())).thenReturn(Optional.ofNullable(mockedUser));
    when(courseRepository.findById(any())).thenReturn(Optional.empty());

    assertThrows(CourseNotFoundException.class, () -> {
      courseService.removeCourse(1L, "OKOK@naver.com");
    });
  }

  @Test
  @DisplayName("사용자가 작성한 데이트 코스 목록을 반환한다")
  public void getDateCoursesMadeByUser() throws Exception {

    User user = User.builder()
        .email("test@naver.com")
        .course(new ArrayList<>())
        .userCourseLikes(new ArrayList<>())
        .build();
    Course course1 = new Course(user, "test1");
    Course course2 = new Course(user, "test2");
    Course course3 = new Course(user, "test3");
    List<Course> list = new ArrayList<>();
    list.add(course1);
    list.add(course2);
    list.add(course3);
    Pageable pageable = PageRequest.of(0, 3);
    Page<Course> page = new PageImpl<>(list, pageable, 3);

    when(userRepository.findByEmail("test@naver.com")).thenReturn(Optional.of(user));
    when(courseRepository.findAllCourseByUserId(user.getId(), pageable)).thenReturn(page);
    Page<RecentCourseDto> myCourse = courseService.getMyCourses("test@naver.com", pageable);

    verify(userRepository, times(1)).findByEmail("test@naver.com");
    assertEquals(myCourse.getContent().size(), 3);
  }

  @Test
  @DisplayName("인가 받지 않은 사용자가 자신이 만든 코스 요청시 예외가 발생한다.")
  public void getMyCourseWithUnAuthorizedUserTest() throws Exception {

    when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> {
      courseService.getMyCourses("NONO@gmail.com", any());
    });
  }

  @Test
  @DisplayName("사용자가 작성한 코스를 삭제한다.")
  public void removeMyCourseTest() throws Exception {
    User mockedUser = mock(User.class);
    Course mockedCourse = mock(Course.class);

    when(userRepository.findByEmail(any())).thenReturn(Optional.ofNullable(mockedUser));
    when(courseRepository.findById(any())).thenReturn(Optional.ofNullable(mockedCourse));

    courseService.removeMyCourse(1L, "OKOK@naver.com");

    verify(courseRepository).delete(mockedCourse);
  }

  @Test
  @DisplayName("인가받지 않은 사용자가 자신이 작성한 코스를 삭제 시도시 예외가 발생한다.")
  public void removeMyCourseWithUnAuthorizedUserTest() throws Exception {

    when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> {
      courseService.removeMyCourse(any(), "NONO@naver.com");
    });
  }

  @Test
  @DisplayName("자신이 작성한 코스에 존재하지 않는 코스를 삭제 시도시 예외가 발생한다.")
  public void removeMyCourseNotExistedTest() throws Exception {

    User mockedUser = mock(User.class);

    when(userRepository.findByEmail(any())).thenReturn(Optional.ofNullable(mockedUser));
    when(courseRepository.findById(any())).thenReturn(Optional.empty());

    assertThrows(CourseNotFoundException.class, () -> {
      courseService.removeMyCourse(1L, "OKOK@naver.com");
    });
  }

  @Test
  @DisplayName("사용자가 저장한 데이트 코스 목록을 반환한다")
  public void getUserSavedDateCourses() throws Exception {

    User user = User.builder()
        .email("test@naver.com")
        .course(new ArrayList<>())
        .userCourseLikes(new ArrayList<>())
        .userCourseSaves(new ArrayList<>())
        .build();
    Course course = new Course(user, "test");
    UserCourseSave userCourseSave = new UserCourseSave(user, course);
    List<UserCourseSave> list = new ArrayList<>();
    list.add(userCourseSave);
    Pageable pageable = PageRequest.of(0, 1);
    Page<UserCourseSave> page = new PageImpl<>(list, pageable, 1);

    when(userRepository.findByEmail("test@naver.com")).thenReturn(Optional.of(user));
    when(userCourseSaveRepository.findAllUserCourseSavePageByUserId(user.getId(), pageable))
        .thenReturn(page);
    Page<SavedCourseDto> savedCourseListResult = courseService
        .getSavedCourses("test@naver.com", pageable);

    verify(userRepository, times(1)).findByEmail("test@naver.com");
    assertEquals(savedCourseListResult.getContent().size(), 1);
  }

  @Test
  @DisplayName("인가 받지 않은 사용자가 저장된 코스 요청시 예외가 발생한다.")
  public void getSavedCoursesWithUnAuthorizedUser() throws Exception {

    when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> {
      courseService.getSavedCourses("NONO@naver.com", any());
    });
  }
}