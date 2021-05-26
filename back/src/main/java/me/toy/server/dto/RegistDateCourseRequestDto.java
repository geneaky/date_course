package me.toy.server.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
public class RegistDateCourseRequestDto {

    @Nullable
    private MultipartFile file;
    private String placeName;
    private float posX;
    private float posY;
    private String text;
}
