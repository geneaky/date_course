package me.toy.server.service;

import static org.assertj.core.api.Assertions.assertThat;
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
import me.toy.server.entity.Comment;
import me.toy.server.entity.Course;
import me.toy.server.entity.Tag;
import me.toy.server.entity.User;
import me.toy.server.entity.UserCourseLike;
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
  public void registDateCourse() throws Exception {

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
  @DisplayName("사진 첨부 없는 데이트 코스 등록 성공")
  public void courseRegistWithoutPhotos() throws Exception {

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
  public void courseRegistWithPhotos() throws Exception {

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
  public void registDateCourseWithPartialPhotos() throws Exception {

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
  public void registDateCourseWithExistedTags() throws Exception {

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
  public void registDateCourseWithNewTags() throws Exception {

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
  public void plusLikeCount() throws Exception {

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
  @DisplayName("좋아요를 누른 코스라면 좋아요를 취소한다")
  public void minusLikeCount() throws Exception {
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
  @DisplayName("데이트 코스에 댓글을 등록 시킨다")
  public void registCommentOnDateCourse() throws Exception {

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
}