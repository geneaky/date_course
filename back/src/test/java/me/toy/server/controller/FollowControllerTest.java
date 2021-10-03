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
import java.util.List;
import me.toy.server.dto.user.UserRequestDto.FollowRequest;
import me.toy.server.dto.user.UserRequestDto.UnfollowRequest;
import me.toy.server.dto.user.UserResponseDto.FolloweeDto;
import me.toy.server.dto.user.UserResponseDto.FollowerDto;
import me.toy.server.dto.user.UserResponseDto.UserFollowees;
import me.toy.server.dto.user.UserResponseDto.UserFollowers;
import me.toy.server.entity.User;
import me.toy.server.service.FollowService;
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

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@WithUserDetails(value = "test@naver.com")
class FollowControllerTest {

  @MockBean
  FollowService followService;
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("사용자가 특정 사용자를 팔로우하는데 성공")
  public void addFollowingUser() throws Exception {

    User user = User.builder()
        .id(3L)
        .name("testOtherUser")
        .email("testOtherUser@naver.com")
        .build();
    FollowRequest followRequest = FollowRequest.builder().followeeId(3L).build();

    mockMvc.perform(post("/followees")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(followRequest)))
        .andDo(print())
        .andExpect(status().isOk());

    verify(followService, times(1)).followUser(any(), any());
  }

  @Test
  @DisplayName("사용자가 팔로잉하는 사용자들 조회")
  public void getUserFollowings() throws Exception {

    FolloweeDto followeeDto1 = new FolloweeDto(1L, "other", "other@naver.com");
    FolloweeDto followeeDto2 = new FolloweeDto(2L, "theother", "theother@naver.com");
    List<FolloweeDto> followeeDtos = new ArrayList<>();
    followeeDtos.add(followeeDto1);
    followeeDtos.add(followeeDto2);
    UserFollowees userFollowees = new UserFollowees(followeeDtos);

    when(followService.getUserFollowees(1L)).thenReturn(userFollowees);

    mockMvc.perform(get("/followees"))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(objectMapper.writeValueAsString(userFollowees)))
        .andDo(print())
        .andExpect(status().isOk());
    verify(followService, times(1)).getUserFollowees(any());
  }

  @Test
  @DisplayName("사용자 팔로우 취소 요청시 취소 성공")
  public void cancleUserFollwing() throws Exception {

    UnfollowRequest unfollowRequest = UnfollowRequest.builder()
        .followeeId(3L)
        .build();

    mockMvc.perform(delete("/followees")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(unfollowRequest)))
        .andDo(print())
        .andExpect(status().isOk());

    verify(followService, times(1)).unfollowUser(any(), any());
  }

  @Test
  @DisplayName("팔로워 목록 조회 요청시 사용자의 팔로워들을 응답에 성공")
  public void getUserFollowers() throws Exception {

    FollowerDto followerDto1 = new FollowerDto(3L, "other", "other@naver.com");
    FollowerDto followerDto2 = new FollowerDto(4L, "people", "people@naver.com");
    List<FollowerDto> list = new ArrayList<>();
    list.add(followerDto1);
    list.add(followerDto2);
    UserFollowers userFollowers = new UserFollowers(list);

    when(followService.getUserFollowers(1L)).thenReturn(userFollowers);

    mockMvc.perform(get("/followers"))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(objectMapper.writeValueAsString(userFollowers)))
        .andDo(print())
        .andExpect(status().isOk());
  }
}