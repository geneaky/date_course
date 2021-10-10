package me.toy.server.security.oauth2.handler;

import static me.toy.server.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

import java.io.IOException;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.toy.server.exception.user.BadRequestException;
import me.toy.server.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import me.toy.server.utils.CookieUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@Component
public class CustomOAuth2AuthenticationSuccessHandler extends
    SimpleUrlAuthenticationSuccessHandler {

  private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {
    String targetUrl = determineTargetUrl(request, response, authentication);

    if (response.isCommitted()) {
      logger.debug("response has already committed.unable to redirect to" + targetUrl);
      return;
    }

    clearAuthenticationAttributes(request, response);
    getRedirectStrategy().sendRedirect(request, response, targetUrl);
  }

  protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) {
    Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
        .map(Cookie::getValue);

    if (!redirectUri.isPresent()) {
      throw new BadRequestException(
          "Unauthorized redirect uri and can't proceed whit th authentication");
    }

    String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

    return UriComponentsBuilder.fromUriString(targetUrl)
        .build().toUriString();
  }

  protected void clearAuthenticationAttributes(HttpServletRequest request,
      HttpServletResponse response) {
    super.clearAuthenticationAttributes(request);
    httpCookieOAuth2AuthorizationRequestRepository
        .removeAuthorizationRequestCookies(request, response);
  }
}
