package me.toy.server.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.toy.server.dto.user.UserRequestDto.UserRegisterForm;
import me.toy.server.dto.user.UserResponseDto.UserDto;
import me.toy.server.entity.User;
import me.toy.server.service.UserService;
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
class UserControllerTest {

  @MockBean
  UserService userService;
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("사용자의 정보를 받아서 회원가입 후 리다이렉션 시킨다.")
  public void registerUserTest() throws Exception {

    UserRegisterForm userRegisterForm = new UserRegisterForm("test@naver.com", "asdf", "testUser");

    mockMvc.perform(post("/user/signUp")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userRegisterForm)))
        .andDo(print())
        .andExpect(redirectedUrl("http://localhost:3000"))
        .andExpect(status().is3xxRedirection());

    verify(userService).createUserAccount(any());
  }

  @Test
  @DisplayName("로그인한 사용자가 사용자 정보 요청시 사용자 정보를 응답한다")
  public void getUserInfo() throws Exception {

    User user = User.builder()
        .name("testUser")
        .email("test@naver.com")
        .build();
    UserDto userDto = new UserDto(user);

    when(userService.getUserInfo("test@naver.com")).thenReturn(userDto);

    mockMvc.perform(get("/user/info"))
        .andDo(print())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(objectMapper.writeValueAsString(userDto)))
        .andExpect(status().isOk());
  }
}