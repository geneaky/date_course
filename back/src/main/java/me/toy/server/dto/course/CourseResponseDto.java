package me.toy.server.dto.course;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.toy.server.entity.Comment;
import me.toy.server.entity.Course;
import me.toy.server.entity.Location;

public class CourseResponseDto {

  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class CourseDto {

    private Long id;
    private int likesCount;
    private String userName;
    private Long userId;
    private String courseTitle;
    private List<Long> userCourseLikesIds;
    private List<LocationDto> locations;
    private List<CommentDto> comments;

    @Builder
    public CourseDto(Course course) {

      this.id = course.getId();
      this.likesCount = course.getUserCourseLikes().size();
      this.courseTitle = course.getCourseTitle();
      this.userId = course.getUser().getId();
      this.userName = course.getUser().getName();
      this.userCourseLikesIds = course.getUser().getUserCourseLikes()
          .stream()
          .map(likeCourse -> likeCourse.getCourse().getId())
          .collect(Collectors.toList());
      this.comments = course.getComments()
          .stream()
          .map(CommentDto::new)
          .collect(Collectors.toList());
      this.locations = course.getLocations()
          .stream()
          .map(LocationDto::new)
          .collect(Collectors.toList());
    }
  }

  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class LocationDto {

    private String locationName;
    private String photoUrl;
    private String text;
    private float posx;
    private float posy;
    private List<String> tags;

    @Builder
    public LocationDto(Location location) {
      this.locationName = location.getName();
      this.photoUrl = location.getPhotoUrl();
      this.text = location.getText();
      this.posx = location.getPosx();
      this.posy = location.getPosy();
      this.tags = location.getLocationTags()
          .stream()
          .map(locationTag -> new String(locationTag.getTag().getName()))
          .collect(Collectors.toList());
    }
  }

  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class CommentDto {

    private String userName;
    private String commentContent;

    @Builder
    public CommentDto(Comment comment) {

      this.userName = comment.getUser().getName();
      this.commentContent = comment.getContent();
    }
  }
}
