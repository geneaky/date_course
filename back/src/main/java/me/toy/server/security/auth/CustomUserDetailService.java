package me.toy.server.security.auth;

import lombok.RequiredArgsConstructor;
import me.toy.server.entity.User;
import me.toy.server.exception.user.UserNotFoundException;
import me.toy.server.repository.UserRepository;
import me.toy.server.security.UserPrincipal;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

//@WithMockUser @WithMockUserDetails의 사용에 필요한 UserDetailsService 구현체
@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

  private final UserRepository userRepository;
  private final PasswordEncoder bCryptPasswordEncoder;

  @Override
  public UserPrincipal
  loadUserByUsername(String email) throws UsernameNotFoundException {

    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("해당 이메일로 가입한 사용자는 없습니다: " + email));

    return UserPrincipal.create(user);
  }

  public UserPrincipal loadUserByEmailAndPassword(String email, String password)
      throws UsernameNotFoundException {

    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UserNotFoundException("해당 이메일로 가입한 사용자는 없습니다: " + email));

    if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
      throw new UserNotFoundException("비밀 번호가 일치하지 않습니다.");
    }

    return UserPrincipal.create(user);
  }
}
