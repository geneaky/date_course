package me.toy.server.service;

import me.toy.server.cloud.S3Uploader;
import me.toy.server.dto.DateCourseRequestDto.RegistDateCourseRequestDto;
import me.toy.server.dto.DateCourseRequestDto.RegistDateCourseRequestDtoList;
import me.toy.server.entity.*;
import me.toy.server.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

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

  private ArrayList<RegistDateCourseRequestDto> createRequestList() {
    ArrayList<RegistDateCourseRequestDto> list = new ArrayList<>();
    return list;
  }

  @Test
  @DisplayName("데이트 코스 등록 성공")
  public void registDateCourse() throws Exception {
    //given
    String userEmail = "test@naver.com";
    String title = "testTitle";
    User user = createUser();
    RegistDateCourseRequestDtoList requestDtoList = mock(RegistDateCourseRequestDtoList.class);
    RegistDateCourseRequestDto requestDto = mock(RegistDateCourseRequestDto.class);
    ArrayList<RegistDateCourseRequestDto> list = createRequestList();
    list.add(requestDto);
    //when
    when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
    when(requestDtoList.getLocationList()).thenReturn(list);
    //then
    dateCourseService.regist(requestDtoList, title, userEmail);
    verify(userRepository, atLeastOnce()).findByEmail(userEmail);
    verify(dateCourseRepository, times(1)).save(any());
    verify(locationRepository, times(1)).save(any());
  }

  @Test
  @DisplayName("사진 첨부 없는 데이트 코스 등록 성공")
  public void dateCourseRegistWithoutPhotos() throws Exception {
    //given
    String userEmail = "test@naver.com";
    String title = "testTitle";
    User user = createUser();
    RegistDateCourseRequestDtoList requestDtoList = mock(RegistDateCourseRequestDtoList.class);
    RegistDateCourseRequestDto requestDto = mock(RegistDateCourseRequestDto.class);
    ArrayList<RegistDateCourseRequestDto> list = createRequestList();
    list.add(requestDto);

    //when
    when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
    when(requestDtoList.getLocationList()).thenReturn(list);
    when(requestDto.getFile()).thenReturn(null);
    //then
    dateCourseService.regist(requestDtoList, title, userEmail);
    verify(dateCourseRepository, atMostOnce()).save(any());
    verify(locationRepository, times(1)).save(any());
    verify(s3Uploader, never()).upload(any(), any());
  }

  @Test
  @DisplayName("사진 첨부된 데이트 코스 등록 성공")
  public void dateCourseRegistWithPhotos() throws Exception {
    //given
    String userEmail = "test@naver.com";
    String title = "testTitle";
    User user = createUser();
    RegistDateCourseRequestDtoList requestDtoList = mock(RegistDateCourseRequestDtoList.class);
    RegistDateCourseRequestDto requestDto = mock(RegistDateCourseRequestDto.class);
    ArrayList<RegistDateCourseRequestDto> list = createRequestList();
    list.add(requestDto);
    //when
    when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
    when(requestDtoList.getLocationList()).thenReturn(list);
    when(requestDto.getFile()).thenReturn(mock(MultipartFile.class));
    //then
    dateCourseService.regist(requestDtoList, title, userEmail);
    verify(locationRepository, times(1)).save(any());
    verify(s3Uploader, times(1)).upload(any(), any());
  }

  @Test
  @DisplayName("사진 첨부가 일부만 되어있는 데이트 코스 등록 성공")
  public void registDateCourseWithPartialPhotos() throws Exception {
    //given
    String userEmail = "test@naver.com";
    String title = "testTitle";
    User user = createUser();
    RegistDateCourseRequestDtoList requestDtoList = mock(RegistDateCourseRequestDtoList.class);
    RegistDateCourseRequestDto requestDto1 = mock(RegistDateCourseRequestDto.class);
    RegistDateCourseRequestDto requestDto2 = mock(RegistDateCourseRequestDto.class);
    RegistDateCourseRequestDto requestDto3 = mock(RegistDateCourseRequestDto.class);
    ArrayList<RegistDateCourseRequestDto> list = createRequestList();
    list.add(requestDto1);
    list.add(requestDto2);
    list.add(requestDto3);
    //when
    when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
    when(requestDtoList.getLocationList()).thenReturn(list);
    when(requestDto1.getFile()).thenReturn(null);
    when(requestDto2.getFile()).thenReturn(mock(MultipartFile.class));
    when(requestDto3.getFile()).thenReturn(mock(MultipartFile.class));
    //then
    dateCourseService.regist(requestDtoList, title, userEmail);
    verify(locationRepository, times(3)).save(any());
    verify(s3Uploader, times(2)).upload(any(), any());
  }

  @Test
  @DisplayName("태그가 걸려있는 데이트 코스 태그 등록 성공")
  public void registDateCourseWithExistedTags() throws Exception {
    //given
    String title = "testTitle";
    String userEmail = "test@naver.com";
    User user = createUser();
    RegistDateCourseRequestDtoList requestDtoList = mock(RegistDateCourseRequestDtoList.class);
    RegistDateCourseRequestDto requestDto = mock(RegistDateCourseRequestDto.class);
    ArrayList<RegistDateCourseRequestDto> list = createRequestList();
    list.add(requestDto);
    List<String> hashTag = new ArrayList<>(Arrays.asList("#hi", "#bi", "#gg"));
    Tag tag1 = new Tag("#hi");
    Tag tag2 = new Tag("#bi");
    Tag tag3 = new Tag("#gg");
    //when
    when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
    when(requestDtoList.getLocationList()).thenReturn(list);
    when(requestDto.getHashTag()).thenReturn(hashTag);
    when(tagRepository.findByName("#hi")).thenReturn(Optional.of(tag1));
    when(tagRepository.findByName("#bi")).thenReturn(Optional.of(tag2));
    when(tagRepository.findByName("#gg")).thenReturn(Optional.of(tag3));
    //then
    dateCourseService.regist(requestDtoList, title, userEmail);
    verify(locationTagRepository, times(3)).save(any());
    verify(tagRepository, never()).save(any());
  }

  @Test
  @DisplayName("새로운 데이트 코스 태그 등록")
  public void registDateCourseWithNewTags() throws Exception {
    //given
    String title = "testTitle";
    String userEmail = "test@naver.com";
    User user = createUser();
    RegistDateCourseRequestDtoList requestDtoList = mock(RegistDateCourseRequestDtoList.class);
    RegistDateCourseRequestDto requestDto = mock(RegistDateCourseRequestDto.class);
    ArrayList<RegistDateCourseRequestDto> list = createRequestList();
    list.add(requestDto);
    List<String> hashTag = new ArrayList<>(Arrays.asList("#hi", "#bi", "#gg"));
    Tag tag1 = new Tag("#hi");
    Tag tag2 = new Tag("#bi");
    //when
    when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
    when(requestDtoList.getLocationList()).thenReturn(list);
    when(requestDto.getHashTag()).thenReturn(hashTag);
    when(tagRepository.findByName("#hi")).thenReturn(Optional.of(tag1));
    when(tagRepository.findByName("#bi")).thenReturn(Optional.of(tag2));
    //then
    dateCourseService.regist(requestDtoList, title, userEmail);
    verify(locationTagRepository, times(3)).save(any());
    verify(tagRepository, times(1)).save(any());
  }

  @Test
  @DisplayName("좋아요를 누르지 않은 코스라면 좋아요 좋아요 카운트를 1 증가 시킨다")
  public void plusLikeCount() throws Exception {
    //given
    String title = "testTitle";
    String userEmail = "test@naver.com";
    User user = createUser();
    DateCourse dateCourse = new DateCourse(user, title);
    List<UserDateCourseLike> userDateCourseLikes = new ArrayList<>();
    UserDateCourseLike userDateCourseLike1 = new UserDateCourseLike(user, dateCourse);
    UserDateCourseLike userDateCourseLike2 = new UserDateCourseLike(user, mock(DateCourse.class));
    UserDateCourseLike userDateCourseLike3 = new UserDateCourseLike(user, mock(DateCourse.class));
    UserDateCourseLike userDateCourseLike4 = new UserDateCourseLike(user, mock(DateCourse.class));
    userDateCourseLikes.add(userDateCourseLike2);
    userDateCourseLikes.add(userDateCourseLike3);
    userDateCourseLikes.add(userDateCourseLike4);
    user.setUserDateCourseLikes(userDateCourseLikes);
    //when
    when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
    when(dateCourseRepository.findById(dateCourse.getId())).thenReturn(Optional.of(dateCourse));
    //then
    dateCourseService.like(dateCourse.getId(), userEmail);
    verify(userDateCourseLikeRepository, times(1)).save(userDateCourseLike1);
  }

  @Test
  @DisplayName("좋아요를 누른 코스라면 좋아요 카운트를 1 감소 시킨다")
  public void minusLikeCount() throws Exception {
    String title = "testTitle";
    String userEmail = "test@naver.com";
    User user = createUser();
    DateCourse dateCourse = new DateCourse(user, title);
    List<UserDateCourseLike> userDateCourseLikes = new ArrayList<>();
    UserDateCourseLike userDateCourseLike1 = new UserDateCourseLike(user, dateCourse);
    UserDateCourseLike userDateCourseLike2 = new UserDateCourseLike(user, mock(DateCourse.class));
    UserDateCourseLike userDateCourseLike3 = new UserDateCourseLike(user, mock(DateCourse.class));
    userDateCourseLikes.add(userDateCourseLike1);
    userDateCourseLikes.add(userDateCourseLike2);
    userDateCourseLikes.add(userDateCourseLike3);
    user.setUserDateCourseLikes(userDateCourseLikes);

    //when
    when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
    when(dateCourseRepository.findById(dateCourse.getId())).thenReturn(Optional.of(dateCourse));
    //then
    dateCourseService.unlike(dateCourse.getId(), userEmail);
    verify(userDateCourseLikeRepository, times(1)).delete(userDateCourseLike1);
    assertThat(user.getUserDateCourseLikes()).doesNotContain(userDateCourseLike1);
    assertThat(dateCourse.getUserDateCourseLikes()).doesNotContain(userDateCourseLike1);
  }

  @Test
  @DisplayName("데이트 코스에 댓글을 등록 시킨다")
  public void registCommentOnDateCourse() throws Exception {
    //given
    String title = "testTitle";
    String userEmail = "test@naver.com";
    String comment = "test comment";
    User user = createUser();
    DateCourse dateCourse = new DateCourse(user, title);
    Comment dateCourseComment = new Comment(user, dateCourse, comment);
    //when
    when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
    when(dateCourseRepository.findById(dateCourse.getId())).thenReturn(Optional.of(dateCourse));
    when(commentRepository.findById(dateCourseComment.getId()))
        .thenReturn(Optional.of(dateCourseComment));
    dateCourseService.registComment(dateCourse.getId(), comment, userEmail);
    //then
    verify(commentRepository, times(1)).save(any());
    assertThat(commentRepository.findById(dateCourseComment.getId()).get().getContent())
        .isEqualTo(comment);
  }
}