package me.toy.server.controller;

import lombok.RequiredArgsConstructor;
import me.toy.server.repository.DateCourseRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@RestController
@RequiredArgsConstructor
public class DataCourseController {

    private final DateCourseRepository dateCourseRepository;

    @PostMapping("/datecourse")
    public void registDateCourse(@RequestParam(value = "normal",required = false) MultipartFile file) {
        System.out.println("file = " + file.getOriginalFilename());
    }
}
