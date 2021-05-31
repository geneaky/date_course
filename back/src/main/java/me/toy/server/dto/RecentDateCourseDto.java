package me.toy.server.dto;

import lombok.Data;
import me.toy.server.entity.DateCourse;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class RecentDateCourseDto {
    private Long id;
    private Long thumbUp;

    private String dateCourseTitle;
    private List<RecentLocationDto> locations;

    public RecentDateCourseDto(DateCourse dateCourse){
        this.id = dateCourse.getId();
        this.thumbUp = dateCourse.getThumbUp();
        this.dateCourseTitle = dateCourse.getDateCourseTitle();
        this.locations = dateCourse.getLocations()
                .stream()
                .map(location -> new RecentLocationDto(location))
                .collect(Collectors.toList());
    }
}
