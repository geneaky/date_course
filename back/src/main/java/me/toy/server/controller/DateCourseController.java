package me.toy.server.controller;

import lombok.RequiredArgsConstructor;
import me.toy.server.dto.*;
import me.toy.server.entity.DateCourse;
import me.toy.server.entity.Location;
import me.toy.server.repository.DateCourseRepository;
import me.toy.server.repository.LocationRepository;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
//@Secured("ROLE_USER")
public class DateCourseController {

    private final DateCourseRepository dateCourseRepository;
    private final LocationRepository locationRepository;

    @PostMapping("/datecourse")
    public void registDateCourse(@RequestPart(value="files",required = false) List<MultipartFile> multipartFiles,
                                 @RequestPart(value="course",required = false) RegistDateCourseRequestDto registDateCourseRequestDto) throws IOException {

        DateCourse dateCourse = new DateCourse(0L);
        dateCourseRepository.save(dateCourse);

        String placeName = registDateCourseRequestDto.getPlaceName();
        String text = registDateCourseRequestDto.getText();
        float posX = registDateCourseRequestDto.getPosX();
        float posY = registDateCourseRequestDto.getPosY();

        Location location = new Location(placeName,text,posX,posY);
        location.setDateCourse(dateCourse);
        List<String> pathList = new ArrayList<>();
        Path directory = Paths.get("src/main/resources/imageUpload/").toAbsolutePath().normalize();

        for(MultipartFile multipartFile : multipartFiles){
            String fileSaveName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
            pathList.add(fileSaveName);
            Path targetPath = directory.resolve(fileSaveName+".jpg").normalize();
            multipartFile.transferTo(targetPath);
        }

        location.setPhotoUrls(pathList);
        locationRepository.save(location);
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
