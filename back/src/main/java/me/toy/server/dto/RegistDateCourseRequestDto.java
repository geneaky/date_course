package me.toy.server.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RegistDateCourseRequestDto {

    private String placeName;
    private String posX;
    private String posY;
    private String text;
}
