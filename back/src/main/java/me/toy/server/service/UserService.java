package me.toy.server.service;

import lombok.RequiredArgsConstructor;
import me.toy.server.dto.user.UserRequestDto.UserRegisterForm;
import me.toy.server.dto.user.UserResponseDto.UserDto;
import me.toy.server.entity.User;
import me.toy.server.exception.user.EmailDuplicationException;
import me.toy.server.exception.user.UserNotFoundException;
import me.toy.server.repository.FollowRepository;
import me.toy.server.repository.UserRepository;
import me.toy.server.security.UserPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final FollowRepository followRepository;
  private final PasswordEncoder bCryptPasswordEncoder;

  @Transactional
  public void createUserAccount(UserRegisterForm userRegisterForm) {
    if (userRepository.findByEmail(userRegisterForm.getEmail()).isPresent()) {
      throw new EmailDuplicationException("해당 이메일로 이미 가입한 사용자 입니다.");
    }

    User newUser = User.builder()
        .email(userRegisterForm.getEmail())
        .password(bCryptPasswordEncoder.encode(userRegisterForm.getPassword()))
        .name(userRegisterForm.getNickName())
        .build();

    userRepository.save(newUser);
  }

  @Transactional(readOnly = true)
  public UserDto getUserInfo(String userEmail) {
    User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
        new UserNotFoundException("그런 이메일로 가입한 사용자는 없습니다.")
    );

    return new UserDto(user);
  }

  @Transactional
  public void withdrawal(UserPrincipal user) {
    followRepository.deleteByFolloweeId(user.getId());
    userRepository.deleteById(user.getId());
  }
}
