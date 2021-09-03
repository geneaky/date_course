package me.toy.server.dto;

import javax.validation.constraints.Digits;
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
}
