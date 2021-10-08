package me.toy.server.config;

import java.util.Optional;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import me.toy.server.entity.User;
import me.toy.server.repository.UserRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@Profile("dev")
public class TestInit {

  private final UserRepository userRepository;
  private final PasswordEncoder bCryptEncoder;

  @PostConstruct
  public void settingUserTest() {

    User user = User.builder()
        .id(1L)
        .email("test@naver.com")
        .name("testUser")
        .password(bCryptEncoder.encode("testUser")).build();
    userRepository.save(user);
  }

  @PreDestroy
  public void clearSettingUserTest() {

    Optional<User> testUser = userRepository.findByEmail("test@naver.com");

    userRepository.delete(testUser.get());
  }

}
