package me.toy.server.security.handler;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.toy.server.exception.security.NoRedirectUriRequestException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final String REDIRECT_URI = "redirect_uri";
  private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {

    String targetUrl = determineTargetUrl(request, response, authentication);
    clearAuthenticationAttributes(request);//인증과정에서 인증 실패 exception을 session에 저장해두기 때문에 비워준다.
    redirectStrategy.sendRedirect(request, response, targetUrl);
//    getRedirectStrategy().sendRedirect(request, response, targetUrl);
  }

  @Override
  protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) {

    String targetUrl = request.getParameter(REDIRECT_URI);

    if (targetUrl == null) {
      throw new NoRedirectUriRequestException("리다이렉트 URI가 포함되지 않은 요청입니다.");
    }
    return targetUrl;
  }
}
