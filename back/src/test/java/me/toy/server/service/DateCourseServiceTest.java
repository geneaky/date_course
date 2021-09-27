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
import me.toy.server.dto.DateCourseRequestDto.RegistDateCourseFormDto;
import me.toy.server.dto.DateCourseRequestDto.RegistLocationFormDto;
import me.toy.server.entity.Comment;
import me.toy.server.entity.DateCourse;
import me.toy.server.entity.Tag;
import me.toy.server.entity.User;
import me.toy.server.entity.UserDateCourseLike;
import me.toy.server.repository.CommentRepository;
import me.toy.server.repository.DateCourseRepository;
import me.toy.server.repository.LocationRepository;
import me.toy.server.repository.LocationTagRepository;
import me.toy.server.repository.TagRepository;
import me.toy.server.repository.UserDateCourseLikeRepository;
import me.toy.server.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class DateCourseServiceTest {

  @Mock
  UserRepository userRepository;
  @Mock
  DateCourseRepository dateCourseRepository;
  @Mock
  LocationRepository locationRepository;
  @Mock
  UserDateCourseLikeRepository userDateCourseLikeRepository;
  @Mock
  CommentRepository commentRepository;
  @Mock
  TagRepository tagRepository;
  @Mock
  LocationTagRepository locationTagRepository;
  @Mock
  S3Uploader s3Uploader;
  @InjectMocks
  DateCourseService dateCourseService;

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
    RegistDateCourseFormDto registDateCourseFormDto = mock(RegistDateCourseFormDto.class);
    RegistLocationFormDto requestDto = mock(RegistLocationFormDto.class);
    ArrayList<RegistLocationFormDto> list = createRequestList();
    list.add(requestDto);

    when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
    when(registDateCourseFormDto.getLocationList()).thenReturn(list);

    dateCourseService.registDateCourse(registDateCourseFormDto, userEmail);

    verify(userRepository, atLeastOnce()).findByEmail(userEmail);
    verify(dateCourseRepository, times(1)).save(any());
    verify(locationRepository, times(1)).saveAll(any());
    verify(tagRepository, times(1)).saveAll(any());
    verify(locationTagRepository, times(1)).saveAll(any());
  }

  @Test
  @DisplayName("사진 첨부 없는 데이트 코스 등록 성공")
  public void dateCourseRegistWithoutPhotos() throws Exception {

    String userEmail = "test@naver.com";
    String title = "testTitle";
    User user = createUser();
    RegistDateCourseFormDto registDateCourseFormDto = mock(RegistDateCourseFormDto.class);
    RegistLocationFormDto requestDto = mock(RegistLocationFormDto.class);
    ArrayList<RegistLocationFormDto> list = createRequestList();
    list.add(requestDto);

    when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
    when(registDateCourseFormDto.getLocationList()).thenReturn(list);
    when(requestDto.getFile()).thenReturn(null);

    dateCourseService.registDateCourse(registDateCourseFormDto, userEmail);

    verify(dateCourseRepository, atMostOnce()).save(any());
    verify(locationRepository, times(1)).saveAll(any());
    verify(tagRepository, times(1)).saveAll(any());
    verify(locationTagRepository, times(1)).saveAll(any());
    verify(s3Uploader, times(1)).upload(any());
  }

  @Test
  @DisplayName("사진 첨부된 데이트 코스 등록 성공")
  public void dateCourseRegistWithPhotos() throws Exception {

    String userEmail = "test@naver.com";
    String title = "testTitle";
    User user = createUser();
    RegistDateCourseFormDto registDateCourseFormDto = mock(RegistDateCourseFormDto.class);
    RegistLocationFormDto requestDto = mock(RegistLocationFormDto.class);
    ArrayList<RegistLocationFormDto> list = createRequestList();
    list.add(requestDto);

    when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
    when(registDateCourseFormDto.getLocationList()).thenReturn(list);
    when(requestDto.getFile()).thenReturn(mock(MultipartFile.class));

    dateCourseService.registDateCourse(registDateCourseFormDto, userEmail);

    verify(dateCourseRepository, atMostOnce()).save(any());
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
    RegistDateCourseFormDto registDateCourseFormDto = mock(RegistDateCourseFormDto.class);
    RegistLocationFormDto requestDto1 = mock(RegistLocationFormDto.class);
    RegistLocationFormDto requestDto2 = mock(RegistLocationFormDto.class);
    RegistLocationFormDto requestDto3 = mock(RegistLocationFormDto.class);
    ArrayList<RegistLocationFormDto> list = createRequestList();
    list.add(requestDto1);
    list.add(requestDto2);
    list.add(requestDto3);

    when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
    when(registDateCourseFormDto.getLocationList()).thenReturn(list);
    when(requestDto1.getFile()).thenReturn(null);
    when(requestDto2.getFile()).thenReturn(mock(MultipartFile.class));
    when(requestDto3.getFile()).thenReturn(mock(MultipartFile.class));

    dateCourseService.registDateCourse(registDateCourseFormDto, userEmail);

    verify(dateCourseRepository, atMostOnce()).save(any());
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
    RegistDateCourseFormDto registDateCourseFormDto = mock(RegistDateCourseFormDto.class);
    RegistLocationFormDto requestDto = mock(RegistLocationFormDto.class);
    ArrayList<RegistLocationFormDto> list = createRequestList();
    list.add(requestDto);
    List<String> hashTag = new ArrayList<>(Arrays.asList("#hi", "#bi", "#gg"));
    Tag tag1 = new Tag("#hi");
    Tag tag2 = new Tag("#bi");
    Tag tag3 = new Tag("#gg");

    when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
    when(registDateCourseFormDto.getLocationList()).thenReturn(list);
    when(requestDto.getHashTag()).thenReturn(hashTag);
    when(tagRepository.findByName("#hi")).thenReturn(Optional.of(tag1));
    when(tagRepository.findByName("#bi")).thenReturn(Optional.of(tag2));
    when(tagRepository.findByName("#gg")).thenReturn(Optional.of(tag3));

    dateCourseService.registDateCourse(registDateCourseFormDto, userEmail);

    verify(dateCourseRepository, atMostOnce()).save(any());
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
    RegistDateCourseFormDto registDateCourseFormDto = mock(RegistDateCourseFormDto.class);
    RegistLocationFormDto requestDto = mock(RegistLocationFormDto.class);
    ArrayList<RegistLocationFormDto> list = createRequestList();
    list.add(requestDto);
    List<String> hashTag = new ArrayList<>(Arrays.asList("#hi", "#bi", "#gg"));
    Tag tag1 = new Tag("#hi");
    Tag tag2 = new Tag("#bi");

    when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
    when(registDateCourseFormDto.getLocationList()).thenReturn(list);
    when(requestDto.getHashTag()).thenReturn(hashTag);
    when(tagRepository.findByName("#hi")).thenReturn(Optional.of(tag1));
    when(tagRepository.findByName("#bi")).thenReturn(Optional.of(tag2));

    dateCourseService.registDateCourse(registDateCourseFormDto, userEmail);

    verify(dateCourseRepository, atMostOnce()).save(any());
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
    DateCourse dateCourse = new DateCourse(user, title);

    when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
    when(dateCourseRepository.findById(dateCourse.getId())).thenReturn(Optional.of(dateCourse));
    when(userDateCourseLikeRepository.findLikeByUserIdAndDateCourseId(user.getId(),
        dateCourse.getId())).thenReturn(Optional.empty());

    dateCourseService.likeDateCourse(dateCourse.getId(), userEmail);

    verify(userDateCourseLikeRepository, times(1)).save(any());
  }

  @Test
  @DisplayName("좋아요를 누른 코스라면 좋아요를 취소한다")
  public void minusLikeCount() throws Exception {
    String title = "testTitle";
    String userEmail = "test@naver.com";
    User user = createUser();
    DateCourse dateCourse = new DateCourse(user, title);
    UserDateCourseLike userDateCourseLike = mock(UserDateCourseLike.class);
    when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
    when(dateCourseRepository.findById(dateCourse.getId())).thenReturn(Optional.of(dateCourse));
    when(userDateCourseLikeRepository.findLikeByUserIdAndDateCourseId(user.getId(),
        dateCourse.getId())).thenReturn(Optional.of(userDateCourseLike));

    dateCourseService.unlikeDateCourse(dateCourse.getId(), userEmail);

    verify(userDateCourseLikeRepository, times(1)).unlikeDateCourse(user.getId(),
        dateCourse.getId());
  }

  @Test
  @DisplayName("데이트 코스에 댓글을 등록 시킨다")
  public void registCommentOnDateCourse() throws Exception {

    String title = "testTitle";
    String userEmail = "test@naver.com";
    String comment = "test comment";
    User user = createUser();
    DateCourse dateCourse = new DateCourse(user, title);
    Comment dateCourseComment = new Comment(user, dateCourse, comment);

    when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
    when(dateCourseRepository.findById(dateCourse.getId())).thenReturn(Optional.of(dateCourse));
    when(commentRepository.findById(dateCourseComment.getId()))
        .thenReturn(Optional.of(dateCourseComment));

    dateCourseService.registComment(dateCourse.getId(), comment, userEmail);

    verify(commentRepository, times(1)).save(any());
    assertThat(commentRepository.findById(dateCourseComment.getId()).get().getContent())
        .isEqualTo(comment);
  }
}