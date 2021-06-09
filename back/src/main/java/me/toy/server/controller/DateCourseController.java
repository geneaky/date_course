package me.toy.server.controller;

import lombok.RequiredArgsConstructor;
import me.toy.server.dto.*;
import me.toy.server.entity.LoginUser;
import me.toy.server.repository.*;
import me.toy.server.service.DateCourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
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

    @GetMapping("/datecourse/recent")
    public List<RecentDateCourseDto> recentDateCourseList(){
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
