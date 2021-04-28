package me.toy.server.controller;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import me.toy.server.dto.UserDto;
import me.toy.server.entity.User;
import me.toy.server.repository.UserRepository;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/user/info")
    @CrossOrigin(originPatterns = "*",allowCredentials = "true",allowedHeaders = "*")
    public UserDto getUserInfo(HttpServletRequest request){
        String jwtHeader = request.getHeader("Authorization");
        String jwtToken = jwtHeader.replace("Bearer ","");
        String username = Jwts.parser().setSigningKey("jsession").parseClaimsJws(jwtToken).getBody().get("username").toString();
        User user = userRepository.findByUsername(username);
        UserDto userInfo = new UserDto(user);
        return userInfo;
    }
}
