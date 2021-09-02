package me.toy.server.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class UserRequestDto {

  @Getter
  @Setter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class AddFollower {

    private Long followerId;

    @Builder
    public AddFollower(Long followerId) {
      this.followerId = followerId;
    }
  }
}
