package me.toy.server.service;

import me.toy.server.dto.UserDto;
import me.toy.server.entity.User;
import me.toy.server.exception.UserNotFoundException;
import me.toy.server.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DataJpaTest
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;


    @Test
    public void 사용자_찾기_실패_예외던지기() throws Exception
    {
        //given
        //when
        assertThrows(UserNotFoundException.class,()->{
            userService.findUser("");
        });
        //then
    }

    @Test
    public void 사용자_필수정보_응답() throws Exception
    {
        //given
        User user = User.builder()
                .id(1L)
                .email("test@naver.com")
                .name("user01")
                .password("nopassword")
                .build();
        Optional<User> oUser = Optional.of(user);
        userRepository.save(user);
        when(userRepository.findByEmail("test@naver.com"))
                .thenReturn(oUser);
        //when
        UserDto userDto = userService.findUser("test@naver.com");
        //then
        assertEquals(userDto.getEmail(),"test@naver.com");
    }
}