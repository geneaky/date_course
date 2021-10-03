package me.toy.server.controller;

import static org.mockito.ArgumentMatchers.any;
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
import java.util.Arrays;
import java.util.List;
import me.toy.server.dto.course.CourseResponseDto.CourseDto;
import me.toy.server.dto.user.UserResponseDto.SavedCourseDto;
import me.toy.server.entity.Course;
import me.toy.server.entity.User;
import me.toy.server.entity.UserCourseSave;
import me.toy.server.service.MyCourseService;
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
class MyCourseControllerTest {

  @MockBean
  private MyCourseService myCourseService;
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("좋아요 누른 데이트 코스 요청시 사용자가 좋아요 누른 데이트 코스 ID 목록을 응답한다")
  public void getUserLikedDateCoure() throws Exception {

    List<Long> likeCourseList = new ArrayList<>(Arrays.asList(1L, 2L, 3L));

    when(myCourseService.getLikedCourseIds("test@naver.com")).thenReturn(likeCourseList);

    mockMvc.perform(get("/my-courses/like/courses/ids"))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(objectMapper.writeValueAsString(likeCourseList)))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("사용자가 코스 저장 요청시 해당 코스를 저장한다")
  public void registUserSavedCourse() throws Exception {

    mockMvc.perform(post("/my-courses/save/courses?courseId=1"))
        .andDo(print())
        .andExpect(status().isOk());

    verify(myCourseService, times(1)).addCourse(any(), any());
  }

  @Test
  @DisplayName("사용자가 저장한 코스를 저장 취소 요청시 저장한 코스에서 삭제한다")
  public void deleteUserSavedCourse() throws Exception {

    mockMvc.perform(delete("/my-courses/save/courses?courseId=1"))
        .andDo(print())
        .andExpect(status().isOk());

    verify(myCourseService, times(1)).removeCourse(any(), any());
  }

  @Test
  @DisplayName("저장한 코스 목록 요청시 사용자가 저장한 코스 ID 목록을 반환한다")
  public void getUserSavedCourse() throws Exception {

    List<Long> savedCourseIDs = new ArrayList<>(Arrays.asList(1L, 2L, 3L, 4L));

    when(myCourseService.getSavedCourseIds("test@naver.com")).thenReturn(savedCourseIDs);

    mockMvc.perform(get("/my-courses/save/courses/ids"))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(objectMapper.writeValueAsString(savedCourseIDs)))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("자신이 작성한 코스 목록을 요청시 사용자가 작성한 코스들 목록을 반환한다")
  public void getMyCourseList() throws Exception {

    User user = User.builder()
        .name("testUser")
        .email("test@naver.com")
        .build();
    Course course1 = new Course(user, "testCousr1");
    Course course2 = new Course(user, "testCousr2");
    CourseDto dateCourseDto1 = new CourseDto(course1);
    CourseDto dateCourseDto2 = new CourseDto(course2);
    List<CourseDto> list = new ArrayList<>();
    list.add(dateCourseDto1);
    list.add(dateCourseDto2);
    Pageable pageable = PageRequest.of(0, 2);
    Page<CourseDto> page = new PageImpl<>(list, pageable, 2);

    when(myCourseService.getMyCourses(1L, pageable)).thenReturn(page);

    mockMvc.perform(get("/my-courses?page=0&size=2"))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(objectMapper.writeValueAsString(page)))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("사용자가 작성한 코스 삭제 요청시 지정한 코스를 삭제한다.")
  public void removeMyCourseTest() throws Exception {

    mockMvc.perform(delete("/my-courses?courseId=1"))
        .andDo(print())
        .andExpect(status().isOk());

    verify(myCourseService).removeMyCourse(any(), any());
  }

  @Test
  @DisplayName("사용자가 저장한 코스 요청시 사용자가 저장한 코스들 목록을 반환한다")
  public void getSavedCourseList() throws Exception {

    User user = User.builder()
        .name("testOtherUser")
        .email("testOtherUser@naver.com")
        .build();
    Course course1 = new Course(user, "testCousr1");
    Course course2 = new Course(user, "testCousr2");
    UserCourseSave userCourseSave1 = new UserCourseSave(user, course1);
    UserCourseSave userCourseSave2 = new UserCourseSave(user, course2);
    SavedCourseDto savedCourseDto1 = new SavedCourseDto(userCourseSave1);
    SavedCourseDto savedCourseDto2 = new SavedCourseDto(userCourseSave2);
    List<SavedCourseDto> list = new ArrayList<>();
    list.add(savedCourseDto1);
    list.add(savedCourseDto2);

    Pageable pageable = PageRequest.of(0, 2);
    Page<SavedCourseDto> page = new PageImpl<>(list, pageable, 2);

    when(myCourseService.getSavedCourses(1L, pageable)).thenReturn(page);

    mockMvc.perform(get("/my-courses/save/courses?page=0&size=2"))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(objectMapper.writeValueAsString(page)))
        .andDo(print())
        .andExpect(status().isOk());
  }
}