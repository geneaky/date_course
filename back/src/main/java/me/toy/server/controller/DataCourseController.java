package me.toy.server.controller;

import lombok.RequiredArgsConstructor;
import me.toy.server.dto.RecentDateCourseDto;
import me.toy.server.dto.ThumbUpDateCourseDto;
import me.toy.server.entity.DateCourse;
import me.toy.server.entity.Location;
import me.toy.server.repository.DateCourseRepository;
import me.toy.server.repository.LocationRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
public class DataCourseController {

    private final DateCourseRepository dateCourseRepository;
    private final LocationRepository locationRepository;

    @PostMapping("/datecourse")
    public void registDateCourse(@RequestParam(value="photos",required = false) List<MultipartFile> multipartFiles,
                                 @RequestParam HashMap<String,String> paramMap) throws IOException {
        String locationName = paramMap.get("locationName");
        String text = paramMap.get("text");
        String posx = paramMap.get("posx");
        String posy = paramMap.get("posy");

        DateCourse dateCourse = new DateCourse(0L);
        dateCourseRepository.save(dateCourse);

        Location location = new Location(locationName,text,posx,posy);
        location.setDateCourse(dateCourse);

        Path directory = Paths.get("src/main/resources/imageUpload/").toAbsolutePath().normalize();
        List<String> pathList = new ArrayList<>();
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
}
