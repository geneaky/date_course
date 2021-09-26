package me.toy.server.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.toy.server.dto.DateCourseRequestDto.RegistDateCourseFormDto;
import me.toy.server.dto.DateCourseResponseDto.LikeOrderDateCourseDto;
import me.toy.server.dto.DateCourseResponseDto.RecentDateCourseDto;
import me.toy.server.entity.LoginUser;
import me.toy.server.service.DateCourseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/datecourses")
@RequiredArgsConstructor
public class DateCourseController {

  private final DateCourseService dateCourseService;

  @Secured("ROLE_USER")
  @PostMapping
  @ApiOperation("데이트 코스 등록")
  public void registDateCourse(
      @ModelAttribute RegistDateCourseFormDto registDateCourseFormDto,
      @LoginUser String userEmail) {

    dateCourseService.registDateCourse(registDateCourseFormDto, userEmail);
  }

  @Secured("ROLE_USER")
  @PostMapping("/{dateCourseId}/like")
  @ApiOperation("데이트 코스 좋아요")
  public void likeDateCourse(@PathVariable Long dateCourseId,
      @LoginUser String userEmail) {

    dateCourseService.likeDateCourse(dateCourseId, userEmail);
  }

  @Secured("ROLE_USER")
  @DeleteMapping("/{dateCourseId}/like")
  @ApiOperation("데이트 코스 좋아요 취소")
  public void unlikeDateCourse(@PathVariable Long dateCourseId,
      @LoginUser String userEmail) {

    dateCourseService.unlikeDateCourse(dateCourseId, userEmail);
  }

  @GetMapping("/recent")
  @ApiOperation("최신순 데이트 코스 제공")
  public Page<RecentDateCourseDto> getRecentOrderDateCourses(Pageable pageable) {

    return dateCourseService.getRecentDateCourses(pageable);
  }

  @GetMapping("/thumbUp")
  @ApiOperation("좋아요순 데이트 코스 제공")
  public Page<LikeOrderDateCourseDto> getLikeOrderDateCourses(Pageable pageable) {

    return dateCourseService.getLikedOrderDateCourses(pageable);
  }

  @Secured("ROLE_USER")
  @PostMapping("/{courseId}/comment")
  @ApiOperation("데이트 코스에 댓글 등록")
  public void registComment(@PathVariable Long courseId,
      String comment,
      @LoginUser String userEmail) {

    dateCourseService.registComment(courseId, comment, userEmail);
  }
}
