package me.toy.server.controller;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import me.toy.server.dto.UserDto;
import me.toy.server.entity.User;
import me.toy.server.repository.UserRepository;
import me.toy.server.security.jwt.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Secured("ROLE_USER")
public class UserController {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/user/info")
    public ResponseEntity<UserDto> getUserInfo(HttpServletRequest request){
        long userId = jwtTokenProvider.getUserIdFromRequest(request);
        Optional<User> oUser = userRepository.findById(userId);
        User user = oUser.get();
        UserDto userInfo = new UserDto(user);
        return ResponseEntity.ok(userInfo);
    }
}
