package me.toy.server.controller;

import static me.toy.server.dto.user.UserRequestDto.UserRegisterForm;

import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.toy.server.annotation.LoginUser;
import me.toy.server.dto.course.CourseResponseDto.RecentCourseDto;
import me.toy.server.dto.user.UserRequestDto.AddFollowerRequest;
import me.toy.server.dto.user.UserRequestDto.RemoveFollowerRequest;
import me.toy.server.dto.user.UserResponseDto.SavedCourseDto;
import me.toy.server.dto.user.UserResponseDto.UserDto;
import me.toy.server.dto.user.UserResponseDto.UserFollowers;
import me.toy.server.dto.user.UserResponseDto.UserFollowings;
import me.toy.server.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Secured("ROLE_USER")
public class UserController {

  private final UserService userService;

  @ApiOperation("사용자 회원가입")
  @PostMapping("/signUp")
  public void registerUser(@Valid UserRegisterForm userRegisterForm, HttpServletResponse response)
      throws IOException {

    userService.createUserAccount(userRegisterForm);
    response.sendRedirect("http://localhost:3000");
  }

  @ApiOperation("로그인한 사용자 정보 제공")
  @GetMapping("/info")
  public ResponseEntity<UserDto> getUserInfo(@LoginUser String userEmail) {

    return ResponseEntity.ok().body(userService.getUserInfo(userEmail));
  }

  @ApiOperation("사용자가 좋아요 누른 데이트 코스ID 리스트 제공")
  @GetMapping("/like/courses/ids")
  public ResponseEntity<List<Long>> getLikedCourseIds(@LoginUser String userEmail) {

    return ResponseEntity.ok().body(userService.getLikedCourseIds(userEmail));
  }

  @ApiOperation("사용자 데이트 코스 저장")
  @PostMapping("/save/courses/{courseId}")
  public void addCourse(@PathVariable Long courseId,
      @LoginUser String userEmail) {

    userService.addCourse(courseId, userEmail);
  }

  @ApiOperation("사용자 저장한 데이트 코스 삭제")
  @DeleteMapping("/save/courses/{courseId}")
  public void removeCourse(@PathVariable Long courseId,
      @LoginUser String userEmail) {

    userService.removeCourse(courseId, userEmail);
  }

  @ApiOperation("사용자가 저장한 데이트 코스 리스트 제공")
  @GetMapping("/save/courses")
  public ResponseEntity<Page<SavedCourseDto>> getSavedCourses(@LoginUser String userEmail,
      Pageable pageable) {

    return ResponseEntity.ok().body(userService.getSavedCourses(userEmail, pageable));
  }

  @ApiOperation("사용자가 저장한 데이트 코스ID 리스트 제공")
  @GetMapping("/save/courses/ids")
  public ResponseEntity<List<Long>> getSavedCourseIds(@LoginUser String userEmail) {

    return ResponseEntity.ok().body(userService.getSavedCourseIds(userEmail));
  }

  @ApiOperation("사용자가 작성한 데이트 코스 제공")
  @GetMapping("/courses")
  public ResponseEntity<Page<RecentCourseDto>> getMyCourses(@LoginUser String userEmail,
      Pageable pageable) {

    return ResponseEntity.ok().body(userService.getMyCourses(userEmail, pageable));
  }

  @ApiOperation("사용자가 작성한 데이트 코스 삭제")
  @DeleteMapping("/courses/{courseId}")
  public void removeMyCourse(@PathVariable Long courseId, @LoginUser String userEmail) {

    userService.removeMyCourse(courseId, userEmail);
  }

  @ApiOperation("사용자 팔로우")
  @PostMapping("/follows")
  public void followUser(@Valid AddFollowerRequest addFollowerRequest,
      @LoginUser String userEmail) {

    userService.followUser(addFollowerRequest, userEmail);
  }

  @ApiOperation("사용자 언팔로우")
  @DeleteMapping("/follows")
  public void unfollowUser(@Valid RemoveFollowerRequest removeFollowerRequest,
      @LoginUser String userEmail) {

    userService.unfollowUser(removeFollowerRequest, userEmail);
  }

  @ApiOperation("사용자 팔로잉 조회")
  @GetMapping("/follows")
  public ResponseEntity<UserFollowings> getUserFollowings(
      @LoginUser String userEmail) {

    return ResponseEntity.ok().body(userService.getUserFollowings(userEmail));
  }

  @ApiOperation("사용자 팔로워 조회")
  @GetMapping("/followers")
  public ResponseEntity<UserFollowers> getUserFollowers(
      @LoginUser String userEmail) {

    return ResponseEntity.ok().body(userService.getUserFollowers(userEmail));
  }
}
