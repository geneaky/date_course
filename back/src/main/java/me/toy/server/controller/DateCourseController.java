package me.toy.server.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.toy.server.dto.*;
import me.toy.server.entity.LoginUser;
import me.toy.server.repository.*;
import me.toy.server.service.DateCourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DateCourseController {
    private final DateCourseRepository dateCourseRepository;
    private final DateCourseService dateCourseService;

    @Secured("ROLE_USER")
    @PostMapping("/datecourse")
    @ApiOperation("데이트 코스 등록")
    public ResponseEntity<?> registDateCourse(@ModelAttribute RegistDateCourseRequestDtoList requestDtoList,
                                              @RequestParam("courseTitle") String title,
                                              @LoginUser String userEmail) {
        dateCourseService.regist(requestDtoList,title,userEmail);
        return ResponseEntity.ok().build();
    }

    @Secured("ROLE_USER")
    @PutMapping("/datecourse/like/{dateCourseId}")
    @ApiOperation("코스의 좋아요 카운트 변경")
    public ResponseEntity<?> updateDateCourseLike(@PathVariable Long dateCourseId,@LoginUser String userEmail){
        dateCourseService.plusOrMinusLike(userEmail,dateCourseId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/datecourse/recent")
    @ApiOperation("최신순 데이트 코스 제공")
    public List<RecentDateCourseDto> recentDateCourseList(){
        return dateCourseRepository.findRecentDatecourse();
    }

    @GetMapping("/datecourse/thumbUp")
    @ApiOperation("좋아요순 데이트 코스 제공")
    public List<ThumbUpDateCourseDto> thumbUpDateCourseList(){
        return dateCourseRepository.findThumbUpDatecourse();
    }

    @GetMapping("/datecourse/currentLocation")//거리순 검색은 아직 명확하지 않음으로 보류
    public List<CurrentLocationDateCourseDto> currentLocationDateCourseDtos(@RequestParam("posX")float posX,
                                                                            @RequestParam("posY")float posY){
        return dateCourseRepository.findCurrentLocationDatecourse(posX,posY);
    }

    @Secured("ROLE_USER")
    @PostMapping("/datecourse/comment/{courseId}")
    @ApiOperation("데이트 코스에 댓글 등록")
    public ResponseEntity<?> registDateCourseComment(@PathVariable Long courseId,
                                                     @RequestParam String comment,
                                                     @LoginUser String userEmail){
        dateCourseService.registComment(courseId,comment,userEmail);
        return ResponseEntity.ok().build();
    }
}
