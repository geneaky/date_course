package me.toy.server.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class UserRequestDto {

  @Getter
  @Setter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class AddFollowerRequest {

    private Long followerId;

    @Builder
    public AddFollowerRequest(Long followerId) {
      this.followerId = followerId;
    }
  }

  @Getter
  @Setter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class RemoveFollowerRequest {

    private Long followerId;

    @Builder
    public RemoveFollowerRequest(Long followerId) {
      this.followerId = followerId;
    }
  }

  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class UserRegisterForm {

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
