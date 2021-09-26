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
import java.util.Arrays;
import java.util.List;
import me.toy.server.dto.DateCourseResponseDto.RecentDateCourseDto;
import me.toy.server.dto.UserRequestDto.AddFollowerRequest;
import me.toy.server.dto.UserRequestDto.RemoveFollowerRequest;
import me.toy.server.dto.UserResponseDto.FollowerUserDto;
import me.toy.server.dto.UserResponseDto.FollowingUserDto;
import me.toy.server.dto.UserResponseDto.SavedDateCourseDto;
import me.toy.server.dto.UserResponseDto.UserDto;
import me.toy.server.dto.UserResponseDto.UserFollowers;
import me.toy.server.dto.UserResponseDto.UserFollowings;
import me.toy.server.entity.DateCourse;
import me.toy.server.entity.User;
import me.toy.server.entity.UserDateCourseSave;
import me.toy.server.service.UserService;
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
class UserControllerTest {

  @MockBean
  UserService userService;
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("로그인한 사용자가 사용자 정보 요청시 사용자 정보를 응답한다")
  public void getUserInfo() throws Exception {
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
  @DisplayName("좋아요 누른 데이트 코스 요청시 사용자가 좋아요 누른 데이트 코스 ID 목록을 응답한다")
  public void getUserLikedDateCoure() throws Exception {
    List<Long> likeCourseList = new ArrayList<>(Arrays.asList(1L, 2L, 3L));
    when(userService.findLikedCourseIds("test@naver.com")).thenReturn(likeCourseList);

    mockMvc.perform(get("/user/like/courses/ids"))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(objectMapper.writeValueAsString(likeCourseList)))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("사용자가 코스 저장 요청시 해당 코스를 저장한다")
  public void registUserSavedCourse() throws Exception {
    mockMvc.perform(post("/user/save/courses/1"))
        .andDo(print())
        .andExpect(status().isOk());

    verify(userService, times(1)).registSavedCourse(any(), any());
  }

  @Test
  @DisplayName("사용자가 저장한 코스를 저장 취소 요청시 저장한 코스에서 삭제한다")
  public void delteUserSavedCourse() throws Exception {
    mockMvc.perform(delete("/user/save/courses/1"))
        .andDo(print())
        .andExpect(status().isOk());

    verify(userService, times(1)).deleteSavedCourse(any(), any());
  }

  @Test
  @DisplayName("저장한 코스 목록 요청시 사용자가 저장한 코스 ID 목록을 반환한다")
  public void getUserSavedCourse() throws Exception {
    List<Long> savedCourseIDs = new ArrayList<>(Arrays.asList(1L, 2L, 3L, 4L));
    when(userService.findSavedCourseIds("test@naver.com")).thenReturn(savedCourseIDs);
    mockMvc.perform(get("/user/save/courses/ids"))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(objectMapper.writeValueAsString(savedCourseIDs)))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("자신이 작성한 코스 목록을 요청시 사용자가 작성한 코스들 목록을 반환한다")
  public void getMyCourseList() throws Exception {

    User user = new User();
    user.setName("testUser");
    user.setEmail("test@naver.com");
    DateCourse dateCourse1 = new DateCourse(user, "testCousr1");
    DateCourse dateCourse2 = new DateCourse(user, "testCousr2");
    RecentDateCourseDto dateCourseDto1 = new RecentDateCourseDto(dateCourse1);
    RecentDateCourseDto dateCourseDto2 = new RecentDateCourseDto(dateCourse2);
    List<RecentDateCourseDto> list = new ArrayList<>();
    list.add(dateCourseDto1);
    list.add(dateCourseDto2);
    Pageable pageable = PageRequest.of(0, 2);
    Page<RecentDateCourseDto> page = new PageImpl<>(list, pageable, 2);

    when(userService.findMyCourse("test@naver.com", pageable)).thenReturn(page);

    mockMvc.perform(get("/user/courses?page=0&size=2"))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(objectMapper.writeValueAsString(page)))
        .andDo(print())
        .andExpect(status().isOk());

  }

  @Test
  @DisplayName("사용자가 저장한 코스 요청시 사용자가 저장한 코스들 목록을 반환한다")
  public void getSavedCourseList() throws Exception {

    User user = new User();
    user.setName("testOtherUser");
    user.setEmail("testOtherUser@naver.com");
    DateCourse dateCourse1 = new DateCourse(user, "testCousr1");
    DateCourse dateCourse2 = new DateCourse(user, "testCousr2");
    UserDateCourseSave userDateCourseSave1 = new UserDateCourseSave(user, dateCourse1);
    UserDateCourseSave userDateCourseSave2 = new UserDateCourseSave(user, dateCourse2);
    SavedDateCourseDto savedDateCourseDto1 = new SavedDateCourseDto(userDateCourseSave1);
    SavedDateCourseDto savedDateCourseDto2 = new SavedDateCourseDto(userDateCourseSave2);
    List<SavedDateCourseDto> list = new ArrayList<>();
    list.add(savedDateCourseDto1);
    list.add(savedDateCourseDto2);

    Pageable pageable = PageRequest.of(0, 2);
    Page<SavedDateCourseDto> page = new PageImpl<>(list, pageable, 2);

    when(userService.findSavedCourseList("test@naver.com", pageable)).thenReturn(page);

    mockMvc.perform(get("/user/save/courses?page=0&size=2"))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(objectMapper.writeValueAsString(page)))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("사용자가 특정 사용자를 팔로우하는데 성공")
  public void addFollowingUser() throws Exception {

    User user = new User();
    user.setId(3L);
    user.setName("testOtherUser");
    user.setEmail("testOtherUser@naver.com");
    AddFollowerRequest addFollowerRequest = AddFollowerRequest.builder().followerId(3L).build();

    mockMvc.perform(post("/user/follows")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(addFollowerRequest)))
        .andDo(print())
        .andExpect(status().isOk());
    verify(userService, times(1)).addFollowerInUserFollowers(any(), any());
  }

  @Test
  @DisplayName("사용자가 팔로잉하는 사용자들 조회")
  public void getUserFollowings() throws Exception {

    FollowingUserDto followingUserDto1 = new FollowingUserDto(1L, "other", "other@naver.com");
    FollowingUserDto followingUserDto2 = new FollowingUserDto(2L, "theother", "theother@naver.com");
    List<FollowingUserDto> followingUserDtos = new ArrayList<>();
    followingUserDtos.add(followingUserDto1);
    followingUserDtos.add(followingUserDto2);
    UserFollowings userFollowings = new UserFollowings(followingUserDtos);

    when(userService.getUserFollowingUsers("test@naver.com")).thenReturn(userFollowings);

    mockMvc.perform(get("/user/follows"))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(objectMapper.writeValueAsString(userFollowings)))
        .andDo(print())
        .andExpect(status().isOk());
    verify(userService, times(1)).getUserFollowingUsers(any());
  }

  @Test
  @DisplayName("사용자 팔로우 취소 요청시 취소 성공")
  public void cancleUserFollwing() throws Exception {

    RemoveFollowerRequest removeFollowerRequest = RemoveFollowerRequest.builder()
        .followerId(3L)
        .build();

    mockMvc.perform(delete("/user/follows")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(removeFollowerRequest)))
        .andDo(print())
        .andExpect(status().isOk());

    verify(userService, times(1)).removeFollowerInUserFollowers(any(), any());
  }

  @Test
  @DisplayName("팔로워 목록 조회 요청시 사용자의 팔로워들을 응답에 성공")
  public void getUserFollowers() throws Exception {

    FollowerUserDto followerUserDto1 = new FollowerUserDto(3L, "other", "other@naver.com");
    FollowerUserDto followerUserDto2 = new FollowerUserDto(4L, "people", "people@naver.com");
    List<FollowerUserDto> list = new ArrayList<>();
    list.add(followerUserDto1);
    list.add(followerUserDto2);
    UserFollowers userFollowers = new UserFollowers(list);

    when(userService.getUserFollowersUsers("test@naver.com")).thenReturn(userFollowers);

    mockMvc.perform(get("/user/followers"))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(objectMapper.writeValueAsString(userFollowers)))
        .andDo(print())
        .andExpect(status().isOk());
  }
}