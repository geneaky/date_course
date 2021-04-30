package me.toy.server.security.handler;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import me.toy.server.security.jwt.JwtTokenProvider;
import me.toy.server.security.oauth2.user.UserPrincipal;
import me.toy.server.security.oauth2.user.GoogleOAuth2UserInfo;
import me.toy.server.security.oauth2.user.OAuth2UserInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    JwtTokenProvider tokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String token = tokenProvider.create(authentication);
        response.addHeader("Authorization","Bearer "+token);
        String targetUrl = "/auth/success";
        RequestDispatcher dis = request.getRequestDispatcher(targetUrl);
        dis.forward(request,response);
    }
}
