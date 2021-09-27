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
  public static class RecentCourseDto {

    private Long id;
    private Long likesCount;
    private String userName;
    private Long userId;
    private String courseTitle;
    private List<Long> userLikedCoursesIds;
    private List<RecentLocationDto> locations;
    private List<CourseCommentDto> comments;

    @Builder
    public RecentCourseDto(Course course) {

      this.id = course.getId();
      this.likesCount = course.getUserCourseLikes().stream().count();
      this.courseTitle = course.getCourseTitle();
      this.userId = course.getUser().getId();
      this.userName = course.getUser().getName();
      this.userLikedCoursesIds = course.getUser().getUserCourseLikes()
          .stream()
          .map(likeCourse -> likeCourse.getCourse().getId())
          .collect(Collectors.toList());
      this.comments = course.getComments()
          .stream()
          .map(comment -> new CourseCommentDto(comment))
          .collect(Collectors.toList());
      this.locations = course.getLocations()
          .stream()
          .map(location -> new RecentLocationDto(location))
          .collect(Collectors.toList());
    }
  }

  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class CourseCommentDto {

    private String userName;
    private String commentContent;

    @Builder
    public CourseCommentDto(Comment comment) {

      this.userName = comment.getUser().getName();
      this.commentContent = comment.getContent();
    }
  }

  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class RecentLocationDto {

    private String locationName;
    private String photoUrl;
    private String text;
    private float posx;
    private float posy;
    private List<String> tags;

    @Builder
    public RecentLocationDto(Location location) {
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
  public static class LikeOrderCourseDto {

    private Long id;
    private int likesCount;
    private List<LikeOrderCourseLocationDto> locations;

    public LikeOrderCourseDto(Long id, int likesCount,
        List<Location> locations) {
      this.id = id;
      this.likesCount = likesCount;
      this.locations = locations.stream()
          .map(LikeOrderCourseLocationDto::new)
          .collect(Collectors.toList());
      ;
    }

//    @Builder
//    public LikeOrderDateCourseDto(DateCourse dateCourse) {
//      this.id = dateCourse.getId();
//      this.likesCount = dateCourse.getUserDateCourseLikes().stream().count();
//      this.locations = dateCourse.getLocations()
//          .stream()
//          .map(LikeOrderDateCourseLocationDto::new)
//          .collect(Collectors.toList());
//    }
  }

  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class LikeOrderCourseLocationDto {

    private String locationName;
    private String photoUrl;
    private String text;
    private float posx;
    private float posy;

    @Builder
    public LikeOrderCourseLocationDto(Location location) {
      this.locationName = location.getName();
      this.photoUrl = location.getPhotoUrl();
      this.text = location.getText();
      this.posx = location.getPosx();
      this.posy = location.getPosy();
    }
  }

  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class CurrentLocationCourseDto {

    private Long id;

    private Long likesCount;

    private List<CurrentLocationDto> locations;

    @Builder
    public CurrentLocationCourseDto(Course course) {
      this.id = course.getId();
      this.likesCount = course.getUserCourseLikes().stream().count();
      this.locations = course.getLocations()
          .stream()
          .map(location -> new CurrentLocationDto(location))
          .collect(Collectors.toList());
    }
  }

  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class CurrentLocationDto {

    private String locationName;
    private String photoUrl;
    private String text;
    private float posx;
    private float posy;

    @Builder
    public CurrentLocationDto(Location location) {
      this.locationName = location.getName();
      this.photoUrl = location.getPhotoUrl();
      this.text = location.getText();
      this.posx = location.getPosx();
      this.posy = location.getPosy();
    }
  }


}
