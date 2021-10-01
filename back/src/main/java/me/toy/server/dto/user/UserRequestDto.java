package me.toy.server.dto.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class UserRequestDto {

  @Getter
  @Setter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class FollowRequest {

    @NotNull
    private Long followeeId;

    @Builder
    public FollowRequest(Long followeeId) {
      this.followeeId = followeeId;
    }
  }

  @Getter
  @Setter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class UnfollowRequest {

    @NotNull
    private Long followeeId;

    @Builder
    public UnfollowRequest(Long followeeId) {
      this.followeeId = followeeId;
    }
  }

  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class
  UserRegisterForm {

    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String password;

    @NotBlank
    private String nickName;

    @Builder
    public UserRegisterForm(String email, String password, String nickName) {
      this.email = email;
      this.password = password;
      this.nickName = nickName;
    }
  }
}
