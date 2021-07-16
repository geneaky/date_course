package me.toy.server.controller;

import me.toy.server.dto.UserDto;
import me.toy.server.entity.LoginUser;
import me.toy.server.entity.User;
import me.toy.server.repository.UserRepository;
import me.toy.server.security.jwt.JwtTokenProvider;
import me.toy.server.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import me.toy.server.security.oauth2.LoginUserArgumentResolver;
import me.toy.server.security.oauth2.user.CustomUserDetailService;
import me.toy.server.security.oauth2.user.UserPrincipal;
import me.toy.server.service.UserService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest()
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

//    @BeforeEach
//    public void setup(){
//        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
//                .apply(springSecurity())
//                .build();
//    }

    @Test
    @DisplayName("사용자 정보를 반환하면 성공")
    @WithUserDetails(value="test@naver.com")
    public void getUserInfo() throws Exception
    {
        mockMvc.perform(get("/user/info"))
                .andExpect(MockMvcResultMatchers.content().json("d"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("사용자가 좋아요 누른 데이트 코스들을 반환하면 성공")
    public void getUserLikedDateCoure() throws Exception
    {
        User user = new User();
        user.setEmail("test@naver.com");
        UserDto userDto = new UserDto(user);

        mockMvc.perform(get("/user/likecourse")
                .param("userEmail","test@naver.com"))
                .andExpect(status().isOk());
    }
}