package me.toy.server.dto;

import me.toy.server.entity.User;

public class UserDto {

    private String name;
    private String email;
    private String profileImage;
    public UserDto(User user) {
        this.name = user.getUsername();
        this.email = user.getEmail();
        this.profileImage = user.getUserProfile();
    }
}
