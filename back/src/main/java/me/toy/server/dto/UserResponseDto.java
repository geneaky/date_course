package me.toy.server.dto;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.toy.server.dto.DateCourseResponseDto.DateCourseCommentDto;
import me.toy.server.dto.DateCourseResponseDto.RecentLocationDto;
import me.toy.server.entity.User;
import me.toy.server.entity.UserDateCourseSave;

public class UserResponseDto {

  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class SavedDateCourseDto {

    private Long id;
    private Long courseLikesCount;
    private String userName;
    private Long userId;
    private String dateCourseTitle;
    private List<Long> userLikedCoursesIds;//boolean isUserLikeCourse로 변경 front랑 같이
    private List<RecentLocationDto> locations;
    private List<DateCourseCommentDto> comments;

    @Builder
    public SavedDateCourseDto(UserDateCourseSave userDateCourseSave) {

      this.id = userDateCourseSave.getDateCourse().getId();
      this.courseLikesCount = userDateCourseSave.getDateCourse().getUserDateCourseLikes().stream()
          .count();//feedback
      this.dateCourseTitle = userDateCourseSave.getDateCourse().getDateCourseTitle();
      this.userId = userDateCourseSave.getDateCourse().getUser().getId();
      this.userName = userDateCourseSave.getDateCourse().getUser().getName();
      this.userLikedCoursesIds = userDateCourseSave.getDateCourse().getUser()
          .getUserDateCourseLikes()
          .stream()
          .map(likeCourse -> likeCourse.getDateCourse().getId())
          .collect(Collectors.toList());
      this.comments = userDateCourseSave.getDateCourse().getComments()
          .stream()
          .map(comment -> new DateCourseCommentDto(comment))
          .collect(Collectors.toList());
      this.locations = userDateCourseSave.getDateCourse().getLocations()
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
  public static class UserFollowings {

    private List<FollowingUserDto> followingUserDtos;

    @Builder
    public UserFollowings(List<FollowingUserDto> followingUserDtos) {
      this.followingUserDtos = followingUserDtos;
    }
  }

  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class FollowingUserDto {

    private Long userId;
    private String name;
    private String email;

    @Builder
    public FollowingUserDto(Long userId, String name, String email) {
      this.userId = userId;
      this.name = name;
      this.email = email;
    }
  }

  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class UserFollowers {

    private List<FollowerUserDto> followerUserDtos;

    @Builder
    public UserFollowers(List<FollowerUserDto> followerUserDtos) {
      this.followerUserDtos = followerUserDtos;
    }
  }

  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class FollowerUserDto {

    private Long userId;
    private String name;
    private String email;

    @Builder
    public FollowerUserDto(Long userId, String name, String email) {
      this.userId = userId;
      this.name = name;
      this.email = email;
    }
  }
}
