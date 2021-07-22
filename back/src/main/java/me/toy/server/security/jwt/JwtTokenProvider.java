package me.toy.server.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import me.toy.server.config.AppProperties;
import me.toy.server.security.oauth2.user.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtTokenProvider {

  private final AppProperties appProperties;

  public String create(Authentication authentication) {
    UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

    Date now = new Date();
    Date expireDate = new Date(now.getTime() + appProperties.getAuth().getTokenExpirationMsec());

    String token = Jwts.builder()
        .setSubject(principal.getEmail())
        .setIssuedAt(new Date())
        .setExpiration(expireDate)
        .signWith(SignatureAlgorithm.HS256, appProperties.getAuth().getTokenSecret()).compact();

    return token;
  }

  public Claims getClaims(String token) {
    Claims claims = Jwts.parser()
        .setSigningKey(appProperties.getAuth().getTokenSecret())
        .parseClaimsJws(token)
        .getBody();

    return claims;
  }

  public Map<String, Object> getUserParseInfo(String token) {
    Claims parseInfo = getClaims(token);
    Map<String, Object> result = new HashMap<>();
    result.put("userEmail", parseInfo.getSubject());
    return result;
  }

  public boolean validateToken(String authToken) {
    try {
      Jwts.parser().setSigningKey(appProperties.getAuth().getTokenSecret())
          .parseClaimsJws(authToken);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }
}
