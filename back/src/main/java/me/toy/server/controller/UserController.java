package me.toy.server.controller;

import io.swagger.annotations.ApiOperation;
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

    @ApiOperation("로그인한 사용자 정보 제공")
    @GetMapping("/user/info")
    public ResponseEntity<UserDto> getUserInfo(@LoginUser String userEmail){
        return ResponseEntity.ok().body(userService.findUser(userEmail));
    }
    @ApiOperation("사용자가 좋아요 누른 데이트 코스ID 리스트 제공")
    @GetMapping("/user/likecourse")
    public ResponseEntity<?> getUserLikedcourse(@LoginUser String userEmail){
        return ResponseEntity.ok().body(userService.findLikedCourse(userEmail));
    }
    @ApiOperation("사용자 데이트 코스 저장")
    @PostMapping("/user/saved/{courseId}")
    public ResponseEntity<?> registUserSavedCourse(@PathVariable Long courseId,@LoginUser String userEmail){
        userService.registSavedCourse(courseId,userEmail);
        return ResponseEntity.ok().build();
    }
    @ApiOperation("사용자 저장한 데이트 코스 삭제")
    @DeleteMapping("/user/saved/{courseId}")
    public ResponseEntity<?>  deleteUserSavedCourse(@PathVariable Long courseId,@LoginUser String userEmail){
        userService.deleteSavedCourse(courseId,userEmail);
        return ResponseEntity.ok().build();
    }
    @ApiOperation("사용자가 저장한 데이트 코스ID 리스트 제공")
    @GetMapping("/user/savedcourse")
    public ResponseEntity<?> getUserSavedCourse(@LoginUser String userEmail){
        return ResponseEntity.ok().body(userService.findSavedCourse(userEmail));
    }
    @ApiOperation("사용자가 만든 데이트 코스 리스트 제공")
    @GetMapping("/user/mycourse")
    public ResponseEntity<?> getMyCourseList(@LoginUser String userEmail){
        return ResponseEntity.ok().body(userService.findMyCourse(userEmail));
    }
    @ApiOperation("사용자가 저장한 데이트 코스 리스트 제공")
    @GetMapping("/user/savedcourse/list")
    public ResponseEntity<?> getSavedCourseList(@LoginUser String userEmail){
        return ResponseEntity.ok().body(userService.findSavedCourseList(userEmail));
    }
}
