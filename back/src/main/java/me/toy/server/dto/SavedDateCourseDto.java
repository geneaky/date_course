package me.toy.server.dto;

import lombok.Data;
import me.toy.server.entity.SavedCourse;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class SavedDateCourseDto {
    private Long id;
    private Long thumbUp;

    private String userName;
    private Long userId;
    private String dateCourseTitle;
    private List<Long> userLikedCourses;
    private List<RecentLocationDto> locations;
    private List<DateCourseCommentDto> comments;

    public SavedDateCourseDto(SavedCourse savedCourse){
        this.id = savedCourse.getDateCourse().getId();
        this.thumbUp = savedCourse.getDateCourse().getThumbUp();
        this.dateCourseTitle = savedCourse.getDateCourse().getDateCourseTitle();
        this.userId = savedCourse.getDateCourse().getUser().getId();
        this.userName = savedCourse.getDateCourse().getUser().getName();
        this.userLikedCourses = savedCourse.getDateCourse().getUser().getLikes()
                .stream()
                .map(likeCourse -> likeCourse.getDateCourse().getId())
                .collect(Collectors.toList());
        this.comments = savedCourse.getDateCourse().getComments()
                .stream()
                .map(comment -> new DateCourseCommentDto(comment))
                .collect(Collectors.toList());
        this.locations = savedCourse.getDateCourse().getLocations()
                .stream()
                .map(location -> new RecentLocationDto(location))
                .collect(Collectors.toList());
    }
}
