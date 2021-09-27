package me.toy.server.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.toy.server.annotation.LoginUser;
import me.toy.server.dto.course.CourseRequestDto.RegistCourseFormDto;
import me.toy.server.dto.course.CourseResponseDto.LikeOrderCourseDto;
import me.toy.server.dto.course.CourseResponseDto.RecentCourseDto;
import me.toy.server.service.CourseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
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
  public Page<RecentCourseDto> getRecentOrderCourses(Pageable pageable) {

    return courseService.getRecentCourses(pageable);
  }

  @GetMapping("/like")
  @ApiOperation("좋아요순 데이트 코스 제공")
  public Page<LikeOrderCourseDto> getLikeOrderCourses(Pageable pageable) {

    return courseService.getLikedOrderCourses(pageable);
  }

  @Secured("ROLE_USER")
  @PostMapping("/{courseId}/comment")
  @ApiOperation("데이트 코스에 댓글 등록")
  public void registComment(@PathVariable Long courseId,
      String comment,
      @LoginUser String userEmail) {

    courseService.registComment(courseId, comment, userEmail);
  }
}
