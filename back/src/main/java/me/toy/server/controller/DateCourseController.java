package me.toy.server.controller;

import lombok.RequiredArgsConstructor;
import me.toy.server.dto.*;
import me.toy.server.entity.DateCourse;
import me.toy.server.entity.Location;
import me.toy.server.entity.User;
import me.toy.server.repository.DateCourseRepository;
import me.toy.server.repository.LocationRepository;
import me.toy.server.repository.UserRepository;
import me.toy.server.security.jwt.JwtTokenProvider;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Secured("ROLE_USER")
public class DateCourseController {

    private final UserRepository userRepository;
    private final DateCourseRepository dateCourseRepository;
    private final LocationRepository locationRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/datecourse")
    public void registDateCourse(@ModelAttribute RegistDateCourseRequestDtoList requestDtoList,
                                 @RequestParam("courseTitle") String title,
                                 @RequestParam("hashTag") String hashTag, HttpServletRequest request) throws IOException {

        long userId = jwtTokenProvider.getUserIdFromRequest(request);
        User user = userRepository.findById(userId).get();

        DateCourse dateCourse = new DateCourse(user,0L,title);
        dateCourseRepository.save(dateCourse);

        Path directory = Paths.get("src/main/resources/imageUpload/").toAbsolutePath().normalize();

        for(RegistDateCourseRequestDto requestDto: requestDtoList.getLocationList()){
            if(requestDto.getFile()==null){
                Location location = new Location(requestDto,"");
                location.setDateCourse(dateCourse);
                locationRepository.save(location);
                continue;
            }
            String fileSaveName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
            Path targetPath = directory.resolve(fileSaveName+".jpg").normalize();
            requestDto.getFile().transferTo(targetPath);
            Location location = new Location(requestDto,fileSaveName);
            location.setDateCourse(dateCourse);
            locationRepository.save(location);
        }
    }

    @GetMapping("/datecourse/recent")
    public List<RecentDateCourseDto> recentDateCourseList(){
        List<RecentDateCourseDto> recentDateCourseList = dateCourseRepository.findRecentDatecourse();
        return recentDateCourseList;
    }

    @GetMapping("/datecourse/thumbUp")
    public List<ThumbUpDateCourseDto> thumbUpDateCourseList(){
        List<ThumbUpDateCourseDto> thumbUpDateCourseList = dateCourseRepository.findThumbUpDatecourse();
        return thumbUpDateCourseList;
    }

    @GetMapping("/datecourse/currentLocation")
    public List<CurrentLocationDateCourseDto> currentLocationDateCourseDtos(@RequestParam("posX")float posX,@RequestParam("posY")float posY){
        List<CurrentLocationDateCourseDto> currentLocationDateCourseDtos = dateCourseRepository.findCurrentLocationDatecourse(posX,posY);
        return currentLocationDateCourseDtos;
    }
}
