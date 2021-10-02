package me.toy.server.controller;

import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import me.toy.server.annotation.LoginUser;
import me.toy.server.dto.course.CourseRequestDto.RegistCourseFormDto;
import me.toy.server.dto.course.CourseResponseDto.CourseDto;
import me.toy.server.dto.user.UserResponseDto.SavedCourseDto;
import me.toy.server.service.CourseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

  private final CourseService courseService;

  @Secured("ROLE_USER")
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @ApiOperation("데이트 코스 등록")
  public void registCourse(
      @ModelAttribute RegistCourseFormDto registCourseFormDto,
      @LoginUser String userEmail) {

    courseService.registCourse(registCourseFormDto, userEmail);
  }

  @Secured("ROLE_USER")
  @PostMapping("/{courseId}/like")
  @ApiOperation("데이트 코스 좋아요")
  public void likeCourse(@PathVariable Long courseId,
      @LoginUser String userEmail) {

    courseService.likeCourse(courseId, userEmail);
  }

  @Secured("ROLE_USER")
  @DeleteMapping("/{courseId}/like")
  @ApiOperation("데이트 코스 좋아요 취소")
  public void unlikeCourse(@PathVariable Long courseId,
      @LoginUser String userEmail) {

    courseService.unlikeCourse(courseId, userEmail);
  }

  @GetMapping("/recent")
  @ApiOperation("최신순 데이트 코스 제공")
  public Page<CourseDto> getRecentOrderCourses(Pageable pageable) {

    return courseService.getRecentCourses(pageable);
  }

  @GetMapping("/like")
  @ApiOperation("좋아요순 데이트 코스 제공")
  public Page<CourseDto> getLikeOrderCourses(Pageable pageable) {

    return courseService.getLikedOrderCourses(pageable);
  }

//  @GetMapping("/tag")
//  @ApiOperation("코스 태그 검색")
//  public Page<CourseResponseDto.CourseDto> searchCoursesByTag(
//      @RequestParam(name = "name") String name,
//      Pageable pageable) {
//
//    return courseService.searchCoursesByTag(name, pageable);
//  }

  @ApiOperation("사용자가 좋아요 누른 데이트 코스ID 리스트 제공")
  @GetMapping("/like/ids")
  public ResponseEntity<List<Long>> getLikedCourseIds(@LoginUser String userEmail) {

    return ResponseEntity.ok().body(courseService.getLikedCourseIds(userEmail));
  }

  @ApiOperation("사용자 데이트 코스 저장")
  @PostMapping("/save/{courseId}")
  public void addCourse(@PathVariable Long courseId,
      @LoginUser String userEmail) {

    courseService.addCourse(courseId, userEmail);
  }

  @ApiOperation("사용자 저장한 데이트 코스 삭제")
  @DeleteMapping("/save/{courseId}")
  public void removeCourse(@PathVariable Long courseId,
      @LoginUser String userEmail) {

    courseService.removeCourse(courseId, userEmail);
  }

  @ApiOperation("사용자가 저장한 데이트 코스 리스트 제공")
  @GetMapping("/save")
  public ResponseEntity<Page<SavedCourseDto>> getSavedCourses(@LoginUser String userEmail,
      Pageable pageable) {

    return ResponseEntity.ok().body(courseService.getSavedCourses(userEmail, pageable));
  }

  @ApiOperation("사용자가 저장한 데이트 코스ID 리스트 제공")
  @GetMapping("/save/ids")
  public ResponseEntity<List<Long>> getSavedCourseIds(@LoginUser String userEmail) {

    return ResponseEntity.ok().body(courseService.getSavedCourseIds(userEmail));
  }

  @ApiOperation("사용자가 작성한 데이트 코스 제공")
  @GetMapping("/my")
  public ResponseEntity<Page<CourseDto>> getMyCourses(@LoginUser String userEmail,
      Pageable pageable) {

    return ResponseEntity.ok().body(courseService.getMyCourses(userEmail, pageable));
  }

  @ApiOperation("사용자가 작성한 데이트 코스 삭제")
  @DeleteMapping("/my/{courseId}")
  public void removeMyCourse(@PathVariable Long courseId, @LoginUser String userEmail) {

    courseService.removeMyCourse(courseId, userEmail);
  }
}
