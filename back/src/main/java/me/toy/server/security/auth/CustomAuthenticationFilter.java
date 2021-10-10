package me.toy.server.security.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.toy.server.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private static final String FORM_EMAIL_KEY = "email";
  private static final String POST = "POST";
  private UserRepository userRepository;

  public CustomAuthenticationFilter(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException {
    if (!request.getMethod().equals(POST)) {
      throw new AuthenticationServiceException("지원하지 않는 요청 메서드입니다: " + request.getMethod());
    }

    return onAuthentication(request);
  }

  private Authentication onAuthentication(HttpServletRequest request) {
    String email = obtainEmail(request);
    String password = obtainPassword(request);
    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
        new UsernamePasswordAuthenticationToken(email, password);

    setDetails(request, usernamePasswordAuthenticationToken);

    return this.getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);
  }

  private String obtainEmail(HttpServletRequest request) {
    return request.getParameter(FORM_EMAIL_KEY);
  }

}
