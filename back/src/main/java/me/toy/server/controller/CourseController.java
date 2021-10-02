package me.toy.server.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.toy.server.annotation.LoginUser;
import me.toy.server.dto.course.CourseRequestDto.RegistCourseFormDto;
import me.toy.server.dto.course.CourseResponseDto.CourseDto;
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
import org.springframework.web.bind.annotation.RequestParam;
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

  @GetMapping
  @ApiOperation("페이징 데이트 코스 제공")
  public Page<CourseDto> getCoursesPage(Pageable pageable) {

    return courseService.getCoursePage(pageable);
  }

  @GetMapping("/tag")
  @ApiOperation("코스 태그 검색")
  public Page<CourseDto> searchCoursesByTag(
      @RequestParam(name = "name") String name,
      Pageable pageable) {

    return courseService.searchCoursesByTag(name, pageable);
  }

  @GetMapping("/title")
  @ApiOperation("코스 제목 검색")
  public Page<CourseDto> searchCoursesByTitle(@RequestParam(name = "title") String title,
      Pageable pageable) {

    return courseService.searchCoursesByTitle(title, pageable);
  }
}
