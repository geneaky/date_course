package me.toy.server.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.toy.server.dto.RecentDateCourseDto;
import me.toy.server.dto.RegistDateCourseRequestDto;
import me.toy.server.dto.RegistDateCourseRequestDtoList;
import me.toy.server.dto.ThumbUpDateCourseDto;
import me.toy.server.entity.DateCourse;
import me.toy.server.entity.User;
import me.toy.server.repository.DateCourseRepository;
import me.toy.server.service.DateCourseService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest()
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@WithUserDetails(value="testUser")
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
    @DisplayName("데이트코스 등록 성공")
    public void registDateCourse() throws Exception
    {
        RegistDateCourseRequestDto requestDto1 = new RegistDateCourseRequestDto();
        RegistDateCourseRequestDto requestDto2= new RegistDateCourseRequestDto();
        requestDto1.setPlaceName("testPlace1");
        requestDto2.setPlaceName("testPlace2");
        ArrayList<RegistDateCourseRequestDto> locationList = new ArrayList<>();
        locationList.add(requestDto1);
        locationList.add(requestDto2);
        RegistDateCourseRequestDtoList requestDtoList = new RegistDateCourseRequestDtoList();
        requestDtoList.setLocationList(locationList);
        mockMvc.perform(post("/datecourse")
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("courseTitle","testCourse")
                .content(objectMapper.writeValueAsString(requestDtoList)))
                .andDo(print())
                .andExpect(status().isOk());

        verify(dateCourseService,times(1)).regist(any(),any(),any());
    }

    @Test
    @DisplayName("사용자가 누른 코스의 좋아요 상태를 변경")
    public void updateDateCourseLike() throws Exception
    {
        mockMvc.perform(put("/datecourse/like/1"))
                .andDo(print())
                .andExpect(status().isOk());
        
        verify(dateCourseService,times(1)).plusOrMinusLike(any(),any());
    }
    
    @Test
    @DisplayName("최신 데이트 코스 리스트 반환")
    public void recentDateCourseList() throws Exception
    {
        User user = new User();
        user.setName("testOtherUser");
        user.setEmail("test@gmail.com");
        DateCourse dateCourse1 = new DateCourse(user,0L,"testCourse1");
        DateCourse dateCourse2 = new DateCourse(user,0L,"testCourse2");
        RecentDateCourseDto recentDateCourseDto1 = new RecentDateCourseDto(dateCourse1);
        RecentDateCourseDto recentDateCourseDto2 = new RecentDateCourseDto(dateCourse2);
        List<RecentDateCourseDto> list = spy(new ArrayList<>());
        list.add(recentDateCourseDto1);
        list.add(recentDateCourseDto2);

        when(dateCourseRepository.findRecentDatecourse()).thenReturn(list);
          mockMvc.perform(get("/datecourse/recent"))
                  .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                  .andExpect(content().json(objectMapper.writeValueAsString(list)))
                  .andDo(print())
                  .andExpect(status().isOk());

          verify(dateCourseRepository,times(1)).findRecentDatecourse();
    }

    @Test
    @DisplayName("좋아요순 데이트 코스 리스트 반환")
    public void thumbUpDAteCourseList() throws Exception
    {
        User user = new User();
        user.setName("testOtherUser");
        user.setEmail("test@gmail.com");
        DateCourse dateCourse1 = new DateCourse(user,0L,"testCourse1");
        DateCourse dateCourse2 = new DateCourse(user,0L,"testCourse2");
        ThumbUpDateCourseDto thumbUpDateCourseDto1 = new ThumbUpDateCourseDto(dateCourse1);
        ThumbUpDateCourseDto thumbUpDateCourseDto2 = new ThumbUpDateCourseDto(dateCourse2);
        List<ThumbUpDateCourseDto> list = spy(new ArrayList<>());
        list.add(thumbUpDateCourseDto1);
        list.add(thumbUpDateCourseDto2);

        when(dateCourseRepository.findThumbUpDatecourse()).thenReturn(list);
        mockMvc.perform(get("/datecourse/thumbUp"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(list)))
                .andDo(print())
                .andExpect(status().isOk());

        verify(dateCourseRepository,times(1)).findThumbUpDatecourse();
    }
    
    @Test
    @DisplayName("데이트 코스에 댓글 등록 성공")
    public void registDateCourseComment() throws Exception
    {
        mockMvc.perform(post("/datecourse/comment/1")
                .param("comment","comment for test!")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        verify(dateCourseService,times(1)).registComment(any(),any(),any());
    }

}