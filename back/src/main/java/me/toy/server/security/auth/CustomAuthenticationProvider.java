package me.toy.server.security.auth;

import lombok.RequiredArgsConstructor;
import me.toy.server.security.UserPrincipal;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

  private final CustomUserDetailService customUserDetailService;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String email = (String) authentication.getPrincipal();
    String password = (String) authentication.getCredentials();
    UserPrincipal userPrincipal = customUserDetailService
        .loadUserByEmailAndPassword(email, password);

    return new UsernamePasswordAuthenticationToken(
        userPrincipal, userPrincipal.getPassword(),
        userPrincipal.getAuthorities());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
  }
}
