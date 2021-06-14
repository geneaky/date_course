package me.toy.server.dto;

import lombok.Data;
import me.toy.server.entity.DateCourse;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class RecentDateCourseDto {
    private Long id;
    private Long thumbUp;

    private String userName;
    private Long userId;
    private String dateCourseTitle;
    private List<Long> userLikedCourses;
    private List<RecentLocationDto> locations;

    public RecentDateCourseDto(DateCourse dateCourse){
        this.id = dateCourse.getId();
        this.thumbUp = dateCourse.getThumbUp();
        this.dateCourseTitle = dateCourse.getDateCourseTitle();
        this.userId = dateCourse.getUser().getId();
        this.userName = dateCourse.getUser().getName();
        this.userLikedCourses = dateCourse.getUser().getLikeCourses()
                .stream()
                .map(likeCourse -> likeCourse.getDateCourse().getId())
                .collect(Collectors.toList());

        this.locations = dateCourse.getLocations()
                .stream()
                .map(location -> new RecentLocationDto(location))
                .collect(Collectors.toList());
    }
}
