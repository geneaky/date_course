package me.toy.server.config.handler;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import me.toy.server.config.auth.PrincipalDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Map<String,Object> claims = new HashMap<>();
        claims.put("username",principalDetails.getAttributes().get("name"));

        String jwtToken = Jwts.builder()
                .setSubject("jsessionId")
                .setExpiration(new Date(System.currentTimeMillis()+1800000))
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512,"jsession")
                .compact();

        response.addHeader("Authorization","Bearer "+jwtToken);
        getRedirectStrategy().sendRedirect(request,response,"http://localhost:3000/login");
//        임시로 지정 나중에 도메인 사면 적용하자
        return ;
    }
}