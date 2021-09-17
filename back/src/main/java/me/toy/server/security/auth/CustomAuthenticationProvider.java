package me.toy.server.security.auth;

import lombok.RequiredArgsConstructor;
import me.toy.server.exception.user.UserNotFoundException;
import me.toy.server.security.UserPrincipal;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

  private final CustomUserDetailService customUserDetailService;
  private final PasswordEncoder bCryptPasswordEncoder;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    String email = (String) authentication.getPrincipal();
    String password = (String) authentication.getCredentials();

    UserPrincipal userPrincipal = customUserDetailService
        .loadUserByUsername(email);

    if (!bCryptPasswordEncoder.matches(password, userPrincipal.getPassword())) {
      throw new UserNotFoundException("비밀 번호가 일치하지 않습니다.");
    }

    return new UsernamePasswordAuthenticationToken(
        userPrincipal, userPrincipal.getPassword(),
        userPrincipal.getAuthorities());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
  }
}
