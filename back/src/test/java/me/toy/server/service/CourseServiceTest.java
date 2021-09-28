package me.toy.server.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
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
import me.toy.server.cloud.S3Uploader;
import me.toy.server.dto.course.CourseRequestDto.RegistCourseFormDto;
import me.toy.server.dto.course.CourseRequestDto.RegistLocationFormDto;
import me.toy.server.dto.course.CourseResponseDto.LikeOrderCourseDto;
import me.toy.server.dto.course.CourseResponseDto.RecentCourseDto;
import me.toy.server.entity.Comment;
import me.toy.server.entity.Course;
import me.toy.server.entity.Tag;
import me.toy.server.entity.User;
import me.toy.server.entity.UserCourseLike;
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
  CommentRepository commentRepository;
  @Mock
  TagRepository tagRepository;
  @Mock
  LocationTagRepository locationTagRepository;
  @Mock
  S3Uploader s3Uploader;
  @InjectMocks
  CourseService courseService;

  private User createUser() {

    String userEmail = "test@naver.com";
    User user = new User();
    user.setEmail(userEmail);

    return user;
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
    verify(s3Uploader, times(1)).upload(any());
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
    verify(s3Uploader, times(1)).upload(any());
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
    verify(s3Uploader, times(3)).upload(any());
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
    verify(s3Uploader, times(1)).upload(any());
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
    verify(s3Uploader, times(1)).upload(any());
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
  @DisplayName("데이트 코스에 댓글을 등록 시킨다")
  public void registCommentTest() throws Exception {

    String title = "testTitle";
    String userEmail = "test@naver.com";
    String comment = "test comment";
    User user = createUser();
    Course course = new Course(user, title);
    Comment dateCourseComment = new Comment(user, course, comment);

    when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
    when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));
    when(commentRepository.findById(dateCourseComment.getId()))
        .thenReturn(Optional.of(dateCourseComment));

    courseService.registComment(course.getId(), comment, userEmail);

    verify(commentRepository, times(1)).save(any());
    assertThat(commentRepository.findById(dateCourseComment.getId()).get().getContent())
        .isEqualTo(comment);
  }

  @Test
  @DisplayName("인가 받지 않은 사용자의 댓글 등록은 예외가 발생합니다.")
  public void registCommentWithUnAuthorizedUser() throws Exception {

    String unAuthorizedUserEmail = "NONONO@gmail.com";
    Long courseId = 1L;
    String comment = "test comment";

    assertThrows(UserNotFoundException.class, () -> {
      courseService.registComment(courseId, comment, unAuthorizedUserEmail);
    });
  }

  @Test
  @DisplayName("존재 하지 않는 코스에 댓글 등록시 예외가 발생합니다.")
  public void registCommentNotExistedCourseTest() throws Exception {

    User mockedUser = mock(User.class);
    String mockedUserEmail = "OKOKOK@gmail.com";
    Long courseId = 1L;
    String comment = "test comment";

    when(userRepository.findByEmail(any())).thenReturn(Optional.ofNullable(mockedUser));

    assertThrows(CourseNotFoundException.class, () -> {
      courseService.registComment(courseId, comment, mockedUserEmail);
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
        .userFollows(new ArrayList<>())
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
        .userFollows(new ArrayList<>())
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
}