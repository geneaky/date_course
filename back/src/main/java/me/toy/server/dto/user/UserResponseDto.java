package me.toy.server.dto.user;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.toy.server.dto.course.CourseResponseDto.CourseCommentDto;
import me.toy.server.dto.course.CourseResponseDto.RecentLocationDto;
import me.toy.server.entity.User;
import me.toy.server.entity.UserCourseSave;

public class UserResponseDto {

  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class SavedCourseDto {

    private Long id;
    private Long courseLikesCount;
    private String userName;
    private Long userId;
    private String courseTitle;
    private List<Long> userLikedCoursesIds;//boolean isUserLikeCourse로 변경 front랑 같이
    private List<RecentLocationDto> locations;
    private List<CourseCommentDto> comments;

    @Builder
    public SavedCourseDto(UserCourseSave userCourseSave) {

      this.id = userCourseSave.getCourse().getId();
      this.courseLikesCount = userCourseSave.getCourse().getUserCourseLikes().stream()
          .count();//feedback
      this.courseTitle = userCourseSave.getCourse().getCourseTitle();
      this.userId = userCourseSave.getCourse().getUser().getId();
      this.userName = userCourseSave.getCourse().getUser().getName();
      this.userLikedCoursesIds = userCourseSave.getCourse().getUser()
          .getUserCourseLikes()
          .stream()
          .map(likeCourse -> likeCourse.getCourse().getId())
          .collect(Collectors.toList());
      this.comments = userCourseSave.getCourse().getComments()
          .stream()
          .map(comment -> new CourseCommentDto(comment))
          .collect(Collectors.toList());
      this.locations = userCourseSave.getCourse().getLocations()
          .stream()
          .map(location -> new RecentLocationDto(location))
          .collect(Collectors.toList());
    }
  }

  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class UserDto {

    private String name;
    private String email;
    private String profileImage;

    @Builder
    public UserDto(User user) {
      this.name = user.getName();
      this.email = user.getEmail();
      this.profileImage = user.getImageUrl();
    }
  }


  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class UserFollowees {

    private List<FolloweeDto> followeeDtos;

    @Builder
    public UserFollowees(List<FolloweeDto> followeeDtos) {
      this.followeeDtos = followeeDtos;
    }
  }

  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class FolloweeDto {

    private Long userId;
    private String name;
    private String email;

    @Builder
    public FolloweeDto(Long userId, String name, String email) {
      this.userId = userId;
      this.name = name;
      this.email = email;
    }
  }

  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class UserFollowers {

    private List<FollowerDto> followerDtos;

    @Builder
    public UserFollowers(List<FollowerDto> followerDtos) {
      this.followerDtos = followerDtos;
    }
  }

  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class FollowerDto {

    private Long userId;
    private String name;
    private String email;

    @Builder
    public FollowerDto(Long userId, String name, String email) {
      this.userId = userId;
      this.name = name;
      this.email = email;
    }
  }
}
