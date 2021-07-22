package me.toy.server.dto;

import lombok.Getter;
import me.toy.server.entity.User;

import javax.validation.constraints.Email;

@Getter
public class UserDto {

  private String name;
  @Email
  private String email;
  private String profileImage;

  public UserDto(User user) {
    this.name = user.getName();
    this.email = user.getEmail();
    this.profileImage = user.getImageUrl();
  }
}
