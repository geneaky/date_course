package me.toy.server.controller;

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
@Log4j2
public class DateCourseController {
    private final DateCourseRepository dateCourseRepository;
    private final DateCourseService dateCourseService;

    @Secured("ROLE_USER")
    @PostMapping("/datecourse")
    public ResponseEntity<?> registDateCourse(@ModelAttribute RegistDateCourseRequestDtoList requestDtoList,
                                              @RequestParam("courseTitle") String title,
                                              @LoginUser String userEmail) {
        dateCourseService.regist(requestDtoList,title,userEmail);
        return ResponseEntity.ok().build();
    }

    @Secured("ROLE_USER")
    @GetMapping("/datecourse/like/{dateCourseId}")
    public ResponseEntity<?> updateDateCourseLike(@PathVariable Long dateCourseId,@LoginUser String userEmail){
        dateCourseService.plusOrMinusLike(userEmail,dateCourseId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/datecourse/recent")
    public List<RecentDateCourseDto> recentDateCourseList(){
        log.warn("최근 데이트 코스 콜");
        return dateCourseRepository.findRecentDatecourse();
    }

    @GetMapping("/datecourse/thumbUp")
    public List<ThumbUpDateCourseDto> thumbUpDateCourseList(){
        return dateCourseRepository.findThumbUpDatecourse();
    }

    @GetMapping("/datecourse/currentLocation")
    public List<CurrentLocationDateCourseDto> currentLocationDateCourseDtos(@RequestParam("posX")float posX,
                                                                            @RequestParam("posY")float posY){
        return dateCourseRepository.findCurrentLocationDatecourse(posX,posY);
    }
}
