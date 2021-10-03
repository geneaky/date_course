package me.toy.server.controller;

import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import me.toy.server.annotation.LoginUser;
import me.toy.server.dto.course.CourseResponseDto.CourseDto;
import me.toy.server.dto.user.UserResponseDto.SavedCourseDto;
import me.toy.server.security.UserPrincipal;
import me.toy.server.service.MyCourseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/my-courses")
@Secured("ROLE_USER")
public class MyCourseController {

  private final MyCourseService myCourseService;

  @ApiOperation("사용자가 좋아요 누른 데이트 코스ID 리스트 제공")
  @GetMapping("/like/courses/ids")
  public ResponseEntity<List<Long>> getLikedCourseIds(@LoginUser UserPrincipal user) {

    return ResponseEntity.ok().body(myCourseService.getLikedCourseIds(user.getEmail()));
  }

  @ApiOperation("사용자 데이트 코스 저장")
  @PostMapping("/save/courses")
  public void addCourse(@RequestParam("courseId") Long courseId,
      @LoginUser UserPrincipal user) {

    myCourseService.addCourse(courseId, user.getEmail());
  }

  @ApiOperation("사용자 저장한 데이트 코스 삭제")
  @DeleteMapping("/save/courses")
  public void removeCourse(@RequestParam("courseId") Long courseId,
      @LoginUser UserPrincipal user) {

    myCourseService.removeCourse(courseId, user.getId());
  }

  @ApiOperation("사용자가 저장한 데이트 코스 리스트 제공")
  @GetMapping("/save/courses")
  public ResponseEntity<Page<SavedCourseDto>> getSavedCourses(@LoginUser UserPrincipal user,
      Pageable pageable) {

    return ResponseEntity.ok().body(myCourseService.getSavedCourses(user.getId(), pageable));
  }

  @ApiOperation("사용자가 저장한 데이트 코스ID 리스트 제공")
  @GetMapping("/save/courses/ids")
  public ResponseEntity<List<Long>> getSavedCourseIds(@LoginUser UserPrincipal user) {

    return ResponseEntity.ok().body(myCourseService.getSavedCourseIds(user.getEmail()));
  }

  @ApiOperation("사용자가 작성한 데이트 코스 제공")
  @GetMapping
  public ResponseEntity<Page<CourseDto>> getMyCourses(@LoginUser UserPrincipal user,
      Pageable pageable) {

    return ResponseEntity.ok().body(myCourseService.getMyCourses(user.getId(), pageable));
  }

  @ApiOperation("사용자가 작성한 데이트 코스 삭제")
  @DeleteMapping
  public void removeMyCourse(@RequestParam("courseId") Long courseId,
      @LoginUser UserPrincipal user) {

    myCourseService.removeMyCourse(courseId, user.getId());
  }
}
