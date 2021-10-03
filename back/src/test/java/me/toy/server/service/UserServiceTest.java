package me.toy.server.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import me.toy.server.dto.user.UserRequestDto.UserRegisterForm;
import me.toy.server.dto.user.UserResponseDto.UserDto;
import me.toy.server.entity.User;
import me.toy.server.exception.user.EmailDuplicationException;
import me.toy.server.exception.user.UserNotFoundException;
import me.toy.server.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @Mock
  UserRepository userRepository;

  @Mock
  PasswordEncoder bCryptPasswordEncorder;

  @InjectMocks
  UserService userService;

  @Test
  @DisplayName("사용자 정보 폼으로 사용자 계정을 생성한다.")
  public void createUserAccountTest() throws Exception {

    UserRegisterForm userRegisterForm = UserRegisterForm.builder()
        .email("test@Naver.com")
        .nickName("testUser")
        .password("asdf")
        .build();
    when(userRepository.findByEmail(userRegisterForm.getEmail())).thenReturn(Optional.empty());

    userService.createUserAccount(userRegisterForm);

    verify(userRepository).save(any());
  }

  @Test
  @DisplayName("이미 가입합 유저가 다시 회원가입을 요청하면 예외가 발생한다.")
  public void createUserAccountWithDuplicatedEmailTest() throws Exception {

    User mockedDuplicatedUser = mock(User.class);
    UserRegisterForm userRegisterForm = mock(UserRegisterForm.class);

    when(userRepository.findByEmail(any())).thenReturn(Optional.ofNullable(mockedDuplicatedUser));

    assertThrows(EmailDuplicationException.class, () -> {
      userService.createUserAccount(userRegisterForm);
    });
  }

  @Test
  @DisplayName("로그인하지 않은 사용자의 정보 요청시 예외가 발생한다")
  public void throwUserNotFoundExceptionWhenUserNotLogin() throws Exception {

    assertThrows(UserNotFoundException.class, () -> {
      userService.getUserInfo(any());
    });
  }

  @Test
  @DisplayName("로그인한 사용자의 필요 정보를 반환한다.")
  public void getLoginUserInfo() throws Exception {

    User user = User.builder()
        .id(1L)
        .email("test@naver.com")
        .name("user01")
        .password("nopassword")
        .build();
    Optional<User> oUser = Optional.of(user);

    when(userRepository.findByEmail(any())).thenReturn(oUser);

    UserDto userDto = userService.getUserInfo("test@naver.com");

    assertEquals(userDto.getEmail(), "test@naver.com");
    verify(userRepository, atLeast(1)).findByEmail(any());
  }
}