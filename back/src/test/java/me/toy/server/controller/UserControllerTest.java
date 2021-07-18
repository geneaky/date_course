package me.toy.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.toy.server.dto.RecentDateCourseDto;
import me.toy.server.dto.SavedDateCourseDto;
import me.toy.server.dto.UserDto;
import me.toy.server.entity.DateCourse;
import me.toy.server.entity.SavedCourse;
import me.toy.server.entity.User;
import me.toy.server.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest()
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@WithUserDetails(value="testUser")
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("사용자 정보를 반환하면 성공")
    public void getUserInfo() throws Exception
    {
        User user = new User();
        user.setName("testUser");
        user.setEmail("test@naver.com");
        UserDto userDto = new UserDto(user);
        when(userService.findUser("test@naver.com")).thenReturn(userDto);
        mockMvc.perform(get("/user/info"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("사용자가 좋아요 누른 데이트 코스들을 반환하면 성공")
    public void getUserLikedDateCoure() throws Exception
    {
        List<Long> likeCourseList = new ArrayList<>(Arrays.asList(1L,2L,3L));
        when(userService.findLikedCourse("test@naver.com")).thenReturn(likeCourseList);

        mockMvc.perform(get("/user/likecourse"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(likeCourseList)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("사용자가 저장하고 싶은 코스를 저장")
    public void registUserSavedCourse() throws Exception
    {
        mockMvc.perform(post("/user/saved/1"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userService,times(1)).registSavedCourse(any(),any());
    }

    @Test
    @DisplayName("사용자가 저장한 코스를 삭제")
    public void delteUserSavedCourse() throws Exception
    {
        mockMvc.perform(delete("/user/saved/1"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userService,times(1)).deleteSavedCourse(any(),any());
    }

    @Test
    @DisplayName("사용자가 저장한 코스 ID 목록 반환")
    public void getUserSavedCourse() throws Exception
    {
        List<Long> savedCourseIDs = new ArrayList<>(Arrays.asList(1L,2L,3L,4L));
        when(userService.findSavedCourse("test@naver.com")).thenReturn(savedCourseIDs);
        mockMvc.perform(get("/user/savedcourse"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(savedCourseIDs)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("사용자가 작성한 코스 리스트 반환")
    public void getMyCourseList() throws Exception
    {
        User user = new User();
        user.setName("testUser");
        user.setEmail("test@naver.com");
        DateCourse dateCourse1 = new DateCourse(user,0L,"testCousr1");
        DateCourse dateCourse2 = new DateCourse(user,0L,"testCousr2");
        RecentDateCourseDto dateCourseDto1 = new RecentDateCourseDto(dateCourse1);
        RecentDateCourseDto dateCourseDto2 = new RecentDateCourseDto(dateCourse2);
        List<RecentDateCourseDto> result = new ArrayList<>();
        result.add(dateCourseDto1);
        result.add(dateCourseDto2);
        when(userService.findMyCourse("test@naver.com")).thenReturn(result);

        mockMvc.perform(get("/user/mycourse"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(result)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("사용자가 저장한 코스 반환")
    public void getSavedCourseList() throws Exception
    {
        User user = new User();
        user.setName("testOtherUser");
        user.setEmail("testOtherUser@naver.com");
        DateCourse dateCourse1 = new DateCourse(user,0L,"testCousr1");
        DateCourse dateCourse2 = new DateCourse(user,0L,"testCousr2");
        SavedCourse savedCourse1 = new SavedCourse(user,dateCourse1);
        SavedCourse savedCourse2 = new SavedCourse(user,dateCourse2);
        SavedDateCourseDto savedDateCourseDto1 = new SavedDateCourseDto(savedCourse1);
        SavedDateCourseDto savedDateCourseDto2 = new SavedDateCourseDto(savedCourse2);
        List<SavedDateCourseDto> result = new ArrayList<>();
        result.add(savedDateCourseDto1);
        result.add(savedDateCourseDto2);

        when(userService.findSavedCourseList("test@naver.com")).thenReturn(result);

        mockMvc.perform(get("/user/savedcourse/list"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(result)))
                .andDo(print())
                .andExpect(status().isOk());
    }
}