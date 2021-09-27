package me.toy.server.dto;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

public class DateCourseRequestDto {

  @Getter
  @Setter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class RegistDateCourseFormDto {

    @NotNull
    private List<RegistLocationFormDto> locationList;
    @NotBlank
    private String courseTitle;

    @Builder
    public RegistDateCourseFormDto(List<RegistLocationFormDto> locationList,
        String courseTitle) {
      this.locationList = locationList;
      this.courseTitle = courseTitle;
    }
  }

  @Getter
  @Setter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class RegistLocationFormDto {

    private MultipartFile file;
    @NotBlank
    private String placeName;
    @NotNull
    private Float posX;
    @NotNull
    private Float posY;
    private String text;
    private List<String> hashTag;

    @Builder
    public RegistLocationFormDto(MultipartFile file, String placeName, Float posX, Float posY,
        String text, List<String> hashTag) {

      this.file = file;
      this.placeName = placeName;
      this.posX = posX;
      this.posY = posY;
      this.text = text;
      this.hashTag = hashTag;
    }
  }

}
