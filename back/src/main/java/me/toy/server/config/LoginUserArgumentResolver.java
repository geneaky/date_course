package me.toy.server.config;

import lombok.RequiredArgsConstructor;
import me.toy.server.annotation.LoginUser;
import me.toy.server.exception.user.UserNotFoundException;
import me.toy.server.security.UserPrincipal;
import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    Authentication authentication = SecurityContextHolder
        .getContext().getAuthentication();
    return parameter.getParameterAnnotation(LoginUser.class) != null
        && authentication instanceof AbstractAuthenticationToken;
  }

  @Override
  public Object resolveArgument(MethodParameter parameter,
      ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      WebDataBinderFactory binderFactory) throws Exception {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication instanceof UsernamePasswordAuthenticationToken
        || authentication instanceof OAuth2AuthenticationToken) {
      return (UserPrincipal) authentication.getPrincipal();
    }

    throw new UserNotFoundException("인증되지 않은 사용자의 요청입니다.");
  }
}
