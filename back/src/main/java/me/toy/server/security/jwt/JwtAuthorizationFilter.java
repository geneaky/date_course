package me.toy.server.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import me.toy.server.security.oauth2.user.UserPrincipal;
import me.toy.server.entity.User;
import me.toy.server.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private UserRepository userRepository;
    private JwtTokenProvider jwtTokenProvider;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        super(authenticationManager);
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("인증이나 권한이 필요한 주소 요청이됨");

        String jwtHeader = request.getHeader("Authorization");

        System.out.println("jwtHeader = " + jwtHeader);

        //header에 jwt가 없으면 그냥 계속 체인을 타서 나가게함
        if(jwtHeader==null||jwtHeader.startsWith("Bearer")){
            chain.doFilter(request,response);
            return;
        }
        //header에 jwt가 있으면 userPrincipal을 찾아서 인가 처리
        Authentication authentication = getUsernamePasswordAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request,response);
    }

    private Authentication getUsernamePasswordAuthentication(HttpServletRequest request){
        String token = request.getHeader("Authorization").replace("Bearer","");

        if(token!=null){
            Claims claims = jwtTokenProvider.getClaims(token);
            Long id = Long.parseLong(claims.getSubject());

            if(id!=null){
                Optional<User> oUser = userRepository.findById(id);
                User user = oUser.get();
                UserPrincipal userPrincipal = UserPrincipal.create(user);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userPrincipal,null,userPrincipal.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                return authentication;
            }
        }
        return null;
    }
}
