package me.toy.server.controller;

import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import me.toy.server.dto.DateCourseResponseDto.RecentDateCourseDto;
import me.toy.server.dto.UserResponseDto.SavedDateCourseDto;
import me.toy.server.dto.UserResponseDto.UserDto;
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
  public ResponseEntity<UserDto> getUserInfo(@LoginUser String userEmail) {

    return ResponseEntity.ok().body(userService.findUser(userEmail));
  }

  @ApiOperation("사용자가 좋아요 누른 데이트 코스ID 리스트 제공")
  @GetMapping("/user/like/courses/ids")
  public ResponseEntity<List<Long>> getUserLikedcourse(@LoginUser String userEmail) {

    return ResponseEntity.ok().body(userService.findLikedCourseIds(userEmail));
  }

  @ApiOperation("사용자 데이트 코스 저장")
  @PostMapping("/user/save/courses/{courseId}")
  public void registUserSavedCourse(@PathVariable Long courseId,
      @LoginUser String userEmail) {

    userService.registSavedCourse(courseId, userEmail);
  }

  @ApiOperation("사용자 저장한 데이트 코스 삭제")
  @DeleteMapping("/user/save/courses/{courseId}")
  public void deleteUserSavedCourse(@PathVariable Long courseId,
      @LoginUser String userEmail) {

    userService.deleteSavedCourse(courseId, userEmail);
  }

  @ApiOperation("사용자가 저장한 데이트 코스ID 리스트 제공")
  @GetMapping("/user/save/courses/ids")
  public ResponseEntity<List<Long>> getUserSavedCourse(@LoginUser String userEmail) {

    return ResponseEntity.ok().body(userService.findSavedCourseIds(userEmail));
  }

  @ApiOperation("사용자가 만든 데이트 코스 리스트 제공")
  @GetMapping("/user/courses")
  public ResponseEntity<List<RecentDateCourseDto>> getMyCourseList(@LoginUser String userEmail) {

    return ResponseEntity.ok().body(userService.findMyCourse(userEmail));
  }

  @ApiOperation("사용자가 저장한 데이트 코스 리스트 제공")
  @GetMapping("/user/save/courses")
  public ResponseEntity<List<SavedDateCourseDto>> getSavedCourseList(@LoginUser String userEmail) {

    return ResponseEntity.ok().body(userService.findSavedCourseList(userEmail));
  }

  @ApiOperation("사용자가 작성한 데이트 코스 삭제")
  @DeleteMapping("/user/courses/{courseId}")
  public void deleteUserCourse(@PathVariable Long courseId, @LoginUser String userEmail) {
    
    userService.deleteCourseMadeByUser(courseId, userEmail);
  }
}
