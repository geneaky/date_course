package me.toy.server.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.toy.server.annotation.LoginUser;
import me.toy.server.dto.course.CourseRequestDto.RegistCourseFormDto;
import me.toy.server.dto.course.CourseResponseDto.CourseDto;
import me.toy.server.security.UserPrincipal;
import me.toy.server.service.CourseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

  @ApiOperation("데이트 코스 등록")
  @Secured("ROLE_USER")
  @PostMapping
  public void registCourse(
      @ModelAttribute RegistCourseFormDto registCourseFormDto,
      @LoginUser UserPrincipal user) {

    courseService.registCourse(registCourseFormDto, user.getEmail());
  }

  @ApiOperation("데이트 코스 좋아요")
  @Secured("ROLE_USER")
  @PostMapping("/{courseId}/like")
  public void likeCourse(@PathVariable Long courseId,
      @LoginUser UserPrincipal user) {

    courseService.likeCourse(courseId, user.getEmail());
  }

  @ApiOperation("데이트 코스 좋아요 취소")
  @Secured("ROLE_USER")
  @DeleteMapping("/{courseId}/like")
  public void unlikeCourse(@PathVariable Long courseId,
      @LoginUser UserPrincipal user) {

    courseService.unlikeCourse(courseId, user.getId());
  }

  @ApiOperation("페이징 데이트 코스 제공")
  @ApiImplicitParam(
      name = "sort",
      value = "정렬 조건 ( id | likes , ASC | DESC )",
      dataType = "string"
  )
  @GetMapping
  public Page<CourseDto> getCoursesPage(Pageable pageable) {

    return courseService.getCoursePage(pageable);
  }

  @ApiOperation("코스 태그 검색")
  @ApiImplicitParam(
      name = "sort",
      value = "정렬 조건 ( id | likes , ASC | DESC )",
      dataType = "string"
  )
  @GetMapping("/tag")
  public Page<CourseDto> searchCoursesByTag(
      @RequestParam(name = "name") String[] names, Pageable pageable) {

    return courseService.searchCoursesByTag(names, pageable);
  }

  @ApiOperation("코스 제목 검색")
  @ApiImplicitParam(
      name = "sort",
      value = "정렬 조건 ( id | likes , ASC | DESC )",
      dataType = "string"
  )
  @GetMapping("/title")
  public Page<CourseDto> searchCoursesByTitle(@RequestParam(name = "title") String title,
      Pageable pageable) {

    return courseService.searchCoursesByTitle(title, pageable);
  }
}
