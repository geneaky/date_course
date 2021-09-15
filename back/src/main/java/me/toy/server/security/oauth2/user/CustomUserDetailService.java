package me.toy.server.security.oauth2.user;

import lombok.RequiredArgsConstructor;
import me.toy.server.entity.User;
import me.toy.server.exception.user.UserNotFoundException;
import me.toy.server.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

//@WithMockUser @WithMockUserDetails의 사용에 필요한 UserDetailsService 구현체
@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

  private final UserRepository userRepository;
  private final PasswordEncoder bCryptPasswordEncoder;

  @Override
  public UserPrincipal loadUserByUsername(String email) throws UsernameNotFoundException {

    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

    return UserPrincipal.create(user);
  }

  public UserPrincipal loadUserByEmailAndPassword(String email, String password)
      throws UsernameNotFoundException {

    User user = userRepository.findByEmailAndPassword(email, bCryptPasswordEncoder.encode(password))
        .orElseThrow(() -> new UserNotFoundException("가입하지 않은 사용자거나 잘못된 정보입니다."));

    return UserPrincipal.create(user);
  }
}
