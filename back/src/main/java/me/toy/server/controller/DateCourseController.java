package me.toy.server.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.toy.server.dto.DateCourseRequestDto.RegistDateCourseFormDto;
import me.toy.server.dto.DateCourseResponseDto.RecentDateCourseDto;
import me.toy.server.dto.DateCourseResponseDto.LikeOrderDateCourseDto;
import me.toy.server.entity.LoginUser;
import me.toy.server.service.DateCourseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DateCourseController {

  private final DateCourseService dateCourseService;

  @Secured("ROLE_USER")
  @PostMapping("/datecourses")
  @ApiOperation("데이트 코스 등록")
  public void registDateCourse(
      @ModelAttribute RegistDateCourseFormDto registDateCourseFormDto,
      @LoginUser String userEmail) {

    dateCourseService.regist(registDateCourseFormDto, userEmail);
  }

  @Secured("ROLE_USER")
  @PostMapping("/datecourses/{dateCourseId}/like")
  @ApiOperation("데이트 코스의 좋아요")
  public void dateCourseLike(@PathVariable Long dateCourseId,
      @LoginUser String userEmail) {

    dateCourseService.like(dateCourseId, userEmail);
  }

  @Secured("ROLE_USER")
  @DeleteMapping("/datecourses/{dateCourseId}/like")
  @ApiOperation("데이트 코스의 좋아요 취소")
  public void dateCourseUnlike(@PathVariable Long dateCourseId,
      @LoginUser String userEmail) {

    dateCourseService.unlike(dateCourseId, userEmail);
  }

  @GetMapping("/datecourses/recent")
  @ApiOperation("최신순 데이트 코스 제공")
  public Page<RecentDateCourseDto> recentDateCourseList(Pageable pageable) {

    return dateCourseService.getRecentDateCourseList(pageable);
  }

  @GetMapping("/datecourses/thumbUp")
  @ApiOperation("좋아요순 데이트 코스 제공")
  public Page<LikeOrderDateCourseDto> likeOrderDateCourseList(Pageable pageable) {

    return dateCourseService.getLikedOrderDateCourseList(pageable);
  }

  @Secured("ROLE_USER")
  @PostMapping("/datecourses/{courseId}/comments")
  @ApiOperation("데이트 코스에 댓글 등록")
  public void registDateCourseComment(@PathVariable Long courseId,
      String comment,
      @LoginUser String userEmail) {

    dateCourseService.registComment(courseId, comment, userEmail);
  }
}
