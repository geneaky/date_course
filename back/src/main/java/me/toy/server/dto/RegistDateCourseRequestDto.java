package me.toy.server.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RegistDateCourseRequestDto {

    private String placeName;
    private float posX;
    private float posY;
    private String text;
}
