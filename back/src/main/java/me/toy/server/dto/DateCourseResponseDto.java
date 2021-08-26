package me.toy.server.dto;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.toy.server.entity.Comment;
import me.toy.server.entity.DateCourse;
import me.toy.server.entity.Location;

public class DateCourseResponseDto {

  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class RecentDateCourseDto {

    private Long id;
    private Long likesCount;
    private String userName;
    private Long userId;
    private String dateCourseTitle;
    private List<Long> userLikedCoursesIds;
    private List<RecentLocationDto> locations;
    private List<DateCourseCommentDto> comments;

    @Builder
    public RecentDateCourseDto(DateCourse dateCourse) {

      this.id = dateCourse.getId();
      this.likesCount = dateCourse.getUserDateCourseLikes().stream().count();
      this.dateCourseTitle = dateCourse.getDateCourseTitle();
      this.userId = dateCourse.getUser().getId();
      this.userName = dateCourse.getUser().getName();
      this.userLikedCoursesIds = dateCourse.getUser().getUserDateCourseLikes()
          .stream()
          .map(likeCourse -> likeCourse.getDateCourse().getId())
          .collect(Collectors.toList());
      this.comments = dateCourse.getComments()
          .stream()
          .map(comment -> new DateCourseCommentDto(comment))
          .collect(Collectors.toList());
      this.locations = dateCourse.getLocations()
          .stream()
          .map(location -> new RecentLocationDto(location))
          .collect(Collectors.toList());
    }
  }

  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class DateCourseCommentDto {

    private String userName;
    private String commentContent;

    @Builder
    public DateCourseCommentDto(Comment comment) {
      
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
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class LikeOrderDateCourseDto {

    private Long id;
    private Long likesCount;
    private List<LikeOrderDateCourseLocationDto> locations;

    @Builder
    public LikeOrderDateCourseDto(DateCourse dateCourse) {
      this.id = dateCourse.getId();
      this.likesCount = dateCourse.getUserDateCourseLikes().stream().count();
      this.locations = dateCourse.getLocations()
          .stream()
          .map(location -> new LikeOrderDateCourseLocationDto(location))
          .collect(Collectors.toList());
    }
  }

  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class LikeOrderDateCourseLocationDto {

    private String locationName;
    private String photoUrl;
    private String text;
    private float posx;
    private float posy;

    @Builder
    public LikeOrderDateCourseLocationDto(Location location) {
      this.locationName = location.getName();
      this.photoUrl = location.getPhotoUrl();
      this.text = location.getText();
      this.posx = location.getPosx();
      this.posy = location.getPosy();
    }
  }

  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class CurrentLocationDateCourseDto {

    private Long id;

    private Long likesCount;

    private List<CurrentLocationDto> locations;

    @Builder
    public CurrentLocationDateCourseDto(DateCourse dateCourse) {
      this.id = dateCourse.getId();
      this.likesCount = dateCourse.getUserDateCourseLikes().stream().count();
      this.locations = dateCourse.getLocations()
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
