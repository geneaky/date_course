package me.toy.server.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import me.toy.server.security.oauth2.user.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class JwtTokenProvider {
    public static final String tokenSecret = "geneaky";
    public static final String tokenExpirationMsec = "86400000";

    public String create(Authentication authentication){
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        LocalDateTime localDateTime = LocalDateTime.now();
        int sec = Integer.parseInt(tokenExpirationMsec)/1000;
        localDateTime = localDateTime.plusSeconds(sec);
        ZoneId defaultZoneId = ZoneId.systemDefault();
        Date expireDate = Date.from(localDateTime.atZone(defaultZoneId).toInstant());

        String token = Jwts.builder()
                .setSubject(Long.toString(principal.getId()))
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256,tokenSecret).compact();

        return token;
    }

    public Claims getClaims(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(tokenSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims;
    }

    public long getUserIdFromToken(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(tokenSecret)
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String authToken){
        try{
            Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(authToken);
            return true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
