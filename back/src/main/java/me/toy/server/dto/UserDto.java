package me.toy.server.dto;

import lombok.Getter;
import me.toy.server.entity.User;

@Getter
public class UserDto {

    private String name;
    private String email;
    private String profileImage;
    public UserDto(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.profileImage = user.getImageUrl();
    }
}
