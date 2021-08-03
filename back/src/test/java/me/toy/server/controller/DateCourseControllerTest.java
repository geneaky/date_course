package me.toy.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.toy.server.dto.DateCourseRequestDto.RegistDateCourseRequestDto;
import me.toy.server.dto.DateCourseRequestDto.RegistDateCourseRequestDtoList;
import me.toy.server.dto.DateCourseResponseDto.RecentDateCourseDto;
import me.toy.server.dto.DateCourseResponseDto.LikeOrderDateCourseDto;
import me.toy.server.entity.DateCourse;
import me.toy.server.entity.User;
import me.toy.server.repository.DateCourseRepository;
import me.toy.server.service.DateCourseService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest()
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@WithUserDetails(value = "testUser")
class DateCourseControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  DateCourseService dateCourseService;

  @MockBean
  DateCourseRepository dateCourseRepository;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("데이트 코스 등록 요청시 데이트 코스와 제목을 받아 등록 시킨다")
  public void registDateCourse() throws Exception {
    RegistDateCourseRequestDto requestDto1 = RegistDateCourseRequestDto.builder()
        .placeName("testPlace1").posX(26).posY(126).build();
    RegistDateCourseRequestDto requestDto2 = RegistDateCourseRequestDto.builder()
        .placeName("testPlace2").posX(28).posY(125).build();
    ArrayList<RegistDateCourseRequestDto> locationList = new ArrayList<>();
    locationList.add(requestDto1);
    locationList.add(requestDto2);
    RegistDateCourseRequestDtoList requestDtoList = RegistDateCourseRequestDtoList.builder()
        .locationList(locationList).build();
    String body = objectMapper.writeValueAsString(requestDtoList);
    mockMvc.perform(post("/datecourses")
        .contentType(MediaType.APPLICATION_JSON)
        .param("courseTitle", "testCourse")
        .content(body))
        .andDo(print())
        .andExpect(status().isOk());

    verify(dateCourseService, times(1)).regist(any(), any(), any());
  }

  @Test
  @DisplayName("사용자가 좋아요 버튼 누를시 해당 코스의 좋아요 값을 1 증가 시킨다")
  public void updateDateCourseLike() throws Exception {
    mockMvc.perform(post("/datecourses/1/like"))
        .andDo(print())
        .andExpect(status().isOk());

    verify(dateCourseService, times(1)).like(any(), any());
  }

  @Test
  @DisplayName("사용자가 좋아요 누른 코스의 좋아요 버튼을 다시 누를시 해당 코스의 좋아요를 1 감소 시킨다")
  public void updateDateCourseUnlike() throws Exception {
    mockMvc.perform(delete("/datecourses/1/like"))
        .andDo(print())
        .andExpect(status().isOk());

    verify(dateCourseService, times(1)).unlike(any(), any());
  }

  @Test
  @DisplayName("최신 데이트 코스 리스트 요청시 최신순 데이트 코스 목록을 반환한다")
  public void recentDateCourseList() throws Exception {
    User user = new User();
    user.setName("testOtherUser");
    user.setEmail("test@gmail.com");
    DateCourse dateCourse1 = new DateCourse(user, "testCourse1");
    DateCourse dateCourse2 = new DateCourse(user, "testCourse2");
    RecentDateCourseDto recentDateCourseDto1 = new RecentDateCourseDto(
        dateCourse1);
    RecentDateCourseDto recentDateCourseDto2 = new RecentDateCourseDto(
        dateCourse2);
    List<RecentDateCourseDto> list = spy(new ArrayList<>());
    list.add(recentDateCourseDto1);
    list.add(recentDateCourseDto2);

    when(dateCourseService.getRecentDateCourseList()).thenReturn(list);
    when(dateCourseRepository.findRecentDateCourse()).thenReturn(list);
    mockMvc.perform(get("/datecourses/recent"))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(objectMapper.writeValueAsString(list)))
        .andDo(print())
        .andExpect(status().isOk());

    verify(dateCourseService, times(1)).getRecentDateCourseList();
  }

  @Test
  @DisplayName("인기 데이트 코스 리스트 요청시 좋아요순 데이트 코스 목록을 반환한다")
  public void likeOrderDateCourseList() throws Exception {
    User user = new User();
    user.setName("testOtherUser");
    user.setEmail("test@gmail.com");
    DateCourse dateCourse1 = new DateCourse(user, "testCourse1");
    DateCourse dateCourse2 = new DateCourse(user, "testCourse2");
    LikeOrderDateCourseDto likeOrderDateCourseDto1 = new LikeOrderDateCourseDto(dateCourse1);
    LikeOrderDateCourseDto likeOrderDateCourseDto2 = new LikeOrderDateCourseDto(dateCourse2);
    List<LikeOrderDateCourseDto> list = spy(new ArrayList<>());
    list.add(likeOrderDateCourseDto1);
    list.add(likeOrderDateCourseDto2);

    when(dateCourseService.getLikedOrderDateCourseList()).thenReturn(list);
    mockMvc.perform(get("/datecourses/thumbUp"))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(objectMapper.writeValueAsString(list)))
        .andDo(print())
        .andExpect(status().isOk());

    verify(dateCourseService, times(1)).getLikedOrderDateCourseList();
  }

  @Test
  @DisplayName("사용자가 데이트 코스에 댓글 입력 요청시 데이트 코스에 댓글 등록시킨다")
  public void registDateCourseComment() throws Exception {
    mockMvc.perform(post("/datecourses/1/comments")
        .content("comment for test!")
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());

    verify(dateCourseService, times(1)).registComment(any(), any(), any());
  }

}