package me.toy.server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class RegistDateCourseRequestDto {

    private MultipartFile files;
    private String placeName;
    private float posX;
    private float posY;
    private String text;
}
