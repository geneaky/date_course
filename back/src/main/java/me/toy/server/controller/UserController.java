package me.toy.server.controller;

import lombok.RequiredArgsConstructor;
import me.toy.server.dto.UserDto;
import me.toy.server.entity.LoginUser;
import me.toy.server.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Secured("ROLE_USER")
public class UserController {

    private final UserService userService;
    @GetMapping("/user/info")
    public ResponseEntity<UserDto> getUserInfo(@LoginUser String userEmail){
        return ResponseEntity.ok().body(userService.findUser(userEmail));
    }
    @GetMapping("/user/likecourse")
    public ResponseEntity<?> getUserLikedcourse(@LoginUser String userEmail){
        return ResponseEntity.ok().body(userService.findLikedCourse(userEmail));
    }

    @PostMapping("/user/saved/{courseId}")
    public ResponseEntity<?> registUserSavedCourse(@PathVariable Long courseId,@LoginUser String userEmail){
        userService.registUserCourse(courseId,userEmail);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/user/saved/{courseId}")
    public ResponseEntity<?>  deleteUserSavedCourse(@PathVariable Long courseId,@LoginUser String userEmail){
        userService.deleteUserCourse(courseId,userEmail);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/savedcourse")
    public ResponseEntity<?> getUserSavedCourse(@LoginUser String userEmail){
        return ResponseEntity.ok().body(userService.findSavedCourse(userEmail));
    }
}
