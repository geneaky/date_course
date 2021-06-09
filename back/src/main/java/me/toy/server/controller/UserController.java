package me.toy.server.controller;

import lombok.RequiredArgsConstructor;
import me.toy.server.dto.UserDto;
import me.toy.server.entity.LoginUser;
import me.toy.server.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Secured("ROLE_USER")
public class UserController {

    private final UserService userService;
    @GetMapping("/user/info")
    public ResponseEntity<UserDto> getUserInfo(@LoginUser String userEmail){
        return ResponseEntity.ok().body(userService.findUser(userEmail));
    }
}
