package me.toy.server.security.oauth2.user;

import lombok.RequiredArgsConstructor;
import me.toy.server.entity.User;
import me.toy.server.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

//@WithMockUser @WithMockUserDetails의 사용에 필요한 UserDetailsService 구현체
@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> oUser = userRepository.findByName(username);

    User user = oUser
        .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

    return UserPrincipal.create(user);
  }
}
