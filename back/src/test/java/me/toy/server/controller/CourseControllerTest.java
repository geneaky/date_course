package me.toy.server.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import me.toy.server.dto.course.CourseRequestDto.RegistCourseFormDto;
import me.toy.server.dto.course.CourseRequestDto.RegistLocationFormDto;
import me.toy.server.dto.course.CourseResponseDto.LikeOrderCourseDto;
import me.toy.server.dto.course.CourseResponseDto.RecentCourseDto;
import me.toy.server.entity.Course;
import me.toy.server.entity.User;
import me.toy.server.repository.CourseRepository;
import me.toy.server.service.CourseService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@WithUserDetails(value = "test@naver.com")
class CourseControllerTest {

  @MockBean
  CourseService courseService;
  @MockBean
  CourseRepository courseRepository;
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("데이트 코스 등록 요청시 데이트 코스와 제목을 받아 등록 시킨다")
  public void registCourse() throws Exception {

    RegistLocationFormDto requestDto1 = RegistLocationFormDto.builder()
        .placeName("testPlace1").posX(26F).posY(126F).build();
    RegistLocationFormDto requestDto2 = RegistLocationFormDto.builder()
        .placeName("testPlace2").posX(28F).posY(125F).build();
    ArrayList<RegistLocationFormDto> locationList = new ArrayList<>();
    locationList.add(requestDto1);
    locationList.add(requestDto2);
    RegistCourseFormDto requestDtoList = RegistCourseFormDto.builder()
        .locationList(locationList).build();
    String body = objectMapper.writeValueAsString(requestDtoList);

    mockMvc.perform(post("/datecourses")
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
            .param("courseTitle", "testCourse")
            .content(body))
        .andDo(print())
        .andExpect(status().isOk());

    verify(courseService, times(1)).registCourse(any(), any());
  }

  @Test
  @DisplayName("사용자가 좋아요 버튼 누를시 해당 코스의 좋아요 값을 1 증가 시킨다")
  public void updateCourseLike() throws Exception {

    mockMvc.perform(post("/datecourses/1/like"))
        .andDo(print())
        .andExpect(status().isOk());

    verify(courseService, times(1)).likeCourse(any(), any());
  }

  @Test
  @DisplayName("사용자가 좋아요 누른 코스의 좋아요 버튼을 다시 누를시 해당 코스의 좋아요를 1 감소 시킨다")
  public void updateCourseUnlike() throws Exception {

    mockMvc.perform(delete("/datecourses/1/like"))
        .andDo(print())
        .andExpect(status().isOk());

    verify(courseService, times(1)).unlikeCourse(any(), any());
  }

  @Test
  @DisplayName("최신 데이트 코스 리스트 요청시 최신순 데이트 코스 목록을 반환한다")
  public void recentCourseList() throws Exception {

    User user = new User();
    user.setName("testOtherUser");
    user.setEmail("test@gmail.com");
    Course course1 = new Course(user, "testCourse1");
    Course course2 = new Course(user, "testCourse2");
    RecentCourseDto recentCourseDto1 = new RecentCourseDto(course1);
    RecentCourseDto recentCourseDto2 = new RecentCourseDto(course2);
    List<RecentCourseDto> list = new ArrayList<>();
    list.add(recentCourseDto1);
    list.add(recentCourseDto2);
    Pageable pageable = PageRequest.of(0, 2);
    Page<RecentCourseDto> page = new PageImpl<>(list, pageable, 2);

    when(courseService.getRecentCourses(pageable)).thenReturn(page);

    mockMvc.perform(get("/datecourses/recent?page=0&size=2"))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(objectMapper.writeValueAsString(page)))
        .andDo(print())
        .andExpect(status().isOk());

    verify(courseService, times(1)).getRecentCourses(pageable);
  }

  @Test
  @DisplayName("인기 데이트 코스 리스트 요청시 좋아요순 데이트 코스 목록을 반환한다")
  public void likeOrderCourseList() throws Exception {

    User user = new User();
    user.setName("testOtherUser");
    user.setEmail("test@gmail.com");
    Course course1 = new Course(user, "testCourse1");
    Course course2 = new Course(user, "testCourse2");
    LikeOrderCourseDto likeOrderCourseDto1 = new LikeOrderCourseDto(course1.getId(),
        course1.getUserCourseLikes().size(),
        course1.getLocations());
    LikeOrderCourseDto likeOrderCourseDto2 = new LikeOrderCourseDto(course2.getId(),
        course2.getUserCourseLikes().size(),
        course2.getLocations());
    List<LikeOrderCourseDto> list = new ArrayList<>();
    list.add(likeOrderCourseDto1);
    list.add(likeOrderCourseDto2);

    Pageable pageable = PageRequest.of(0, 2);
    Page<LikeOrderCourseDto> page = new PageImpl<>(list, pageable, 2);

    when(courseService.getLikedOrderCourses(pageable)).thenReturn(page);

    mockMvc.perform(get("/datecourses/thumbUp?page=0j&size=2"))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(objectMapper.writeValueAsString(page)))
        .andDo(print())
        .andExpect(status().isOk());

    verify(courseService, times(1)).getLikedOrderCourses(pageable);
  }

  @Test
  @DisplayName("사용자가 데이트 코스에 댓글 입력 요청시 데이트 코스에 댓글 등록시킨다")
  public void registCourseComment() throws Exception {

    mockMvc.perform(post("/datecourses/1/comment")
            .content("comment for test!")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());

    verify(courseService, times(1)).registComment(any(), any(), any());
  }

}