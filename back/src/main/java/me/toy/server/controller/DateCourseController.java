package me.toy.server.controller;

import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.toy.server.dto.DateCourseRequestDto.RegistDateCourseRequestDtoList;
import me.toy.server.dto.DateCourseResponseDto.CurrentLocationDateCourseDto;
import me.toy.server.dto.DateCourseResponseDto.RecentDateCourseDto;
import me.toy.server.dto.DateCourseResponseDto.LikeOrderDateCourseDto;
import me.toy.server.entity.LoginUser;
import me.toy.server.repository.*;
import me.toy.server.service.DateCourseService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DateCourseController {

  private final DateCourseRepository dateCourseRepository;
  private final DateCourseService dateCourseService;

  @Secured("ROLE_USER")
  @PostMapping("/datecourses")
  @ApiOperation("데이트 코스 등록")
  public void registDateCourse(
      @LoginUser String userEmail,
      @RequestParam("courseTitle") String title,
      @Valid @ModelAttribute RegistDateCourseRequestDtoList requestDtoList) {
    dateCourseService.regist(requestDtoList, title, userEmail);
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
  public List<RecentDateCourseDto> recentDateCourseList() {
    return dateCourseService.getRecentDateCourseList();
  }

  @GetMapping("/datecourses/thumbUp")
  @ApiOperation("좋아요순 데이트 코스 제공")
  public List<LikeOrderDateCourseDto> likeOrderDateCourseList() {
    return dateCourseService.getLikedOrderDateCourseList();
  }

  @GetMapping("/datecourses/currentLocation")//거리순 검색은 아직 명확하지 않음으로 보류
  public List<CurrentLocationDateCourseDto> currentLocationDateCourseDtos(
      @RequestParam("posX") float posX,
      @RequestParam("posY") float posY) {
    return dateCourseRepository.findCurrentLocationDateCourse(posX, posY);
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
