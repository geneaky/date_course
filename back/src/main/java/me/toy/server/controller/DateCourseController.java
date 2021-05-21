package me.toy.server.controller;

import lombok.RequiredArgsConstructor;
import me.toy.server.dto.*;
import me.toy.server.repository.DateCourseRepository;
import me.toy.server.repository.LocationRepository;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
//@Secured("ROLE_USER")
public class DateCourseController {

    private final DateCourseRepository dateCourseRepository;
    private final LocationRepository locationRepository;

    @PostMapping("/datecourse")
    public void registDateCourse(@RequestPart(value="files",required = false) List<MultipartFile> multipartFiles,
                                 @RequestPart(value="course",required = false) CourseRegisterRequestDto courseRegisterRequestDto) throws IOException {
        // 필요 속성만 추출해서 requestdto 다시 생성
//        String locationName = paramMap.get("locationName");
//        String text = paramMap.get("text");
//        float posx = Float.parseFloat(paramMap.get("posx"));
//        float posy = Float.parseFloat(paramMap.get("posy"));
//
//        DateCourse dateCourse = new DateCourse(0L);
//        dateCourseRepository.save(dateCourse);
//
//        Location location = new Location(locationName,text,posx,posy);
//        location.setDateCourse(dateCourse);

        String placeName = courseRegisterRequestDto.getLocation().getPlace().getPlaceName();
        System.out.println(placeName);

        Path directory = Paths.get("src/main/resources/imageUpload/").toAbsolutePath().normalize();
        List<String> pathList = new ArrayList<>();
        for(MultipartFile multipartFile : multipartFiles){
            String fileSaveName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
            pathList.add(fileSaveName);
            Path targetPath = directory.resolve(fileSaveName+".jpg").normalize();
            multipartFile.transferTo(targetPath);
        }

//        location.setPhotoUrls(pathList);
//        locationRepository.save(location);
    }

//    @PostMapping("/datecourse")
//    public void registDateCourse(@RequestBody CourseRegisterRequestDto courseRegisterRequestDto) throws IOException {
//        List<LocationRequest> locations = courseRegisterRequestDto.getCourse();
//
//        Path directory = Paths.get("src/main/resources/imageUpload/").toAbsolutePath().normalize();
//        List<String> pathList = new ArrayList<>();
//        List<MultipartFile> photos = null;
//        for(LocationRequest lr:locations){
//            String placeName = lr.getPlace().getPlaceName();
//            System.out.println(placeName);
//        }
//
//        if(photos!=null){
//        for(MultipartFile multipartFile : photos){
//            String fileSaveName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
//            pathList.add(fileSaveName);
//            Path targetPath = directory.resolve(fileSaveName+".jpg").normalize();
//            multipartFile.transferTo(targetPath);
//        }
//        }
//    }

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
