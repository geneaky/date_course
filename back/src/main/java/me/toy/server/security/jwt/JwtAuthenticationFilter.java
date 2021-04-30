package me.toy.server.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import me.toy.server.payload.LoginRequest;
import me.toy.server.repository.UserRepository;
import me.toy.server.security.oauth2.user.CustomUserDetailService;
import me.toy.server.security.oauth2.user.UserPrincipal;
import me.toy.server.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CustomUserDetailService customUserDetailService;


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        System.out.println("로그인 시도중");

        LoginRequest credentials = null;

        try{
            credentials = new ObjectMapper().readValue(request.getInputStream(),LoginRequest.class);
        }catch (IOException e){
            e.printStackTrace();
        }
        Optional<User> oUser = userRepository.findByEmail(credentials.getEmail());
        User user = oUser.get();
        UserDetails userDetails = customUserDetailService.loadUserByUsername(user.getEmail());
        //create login token
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        credentials.getPassword());

        //authenticate User
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication); //인가처리를위해 세션에 넣기
        return authentication;
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        String token = jwtTokenProvider.create(authentication);
        response.setHeader("Authorization","Bearer "+token);
    }
}
