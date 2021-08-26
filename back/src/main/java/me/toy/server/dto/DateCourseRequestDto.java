package me.toy.server.dto;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

public class DateCourseRequestDto {

  @Getter
  @Setter
  @NoArgsConstructor
  public static class RegistDateCourseRequestDtoList {

    private List<RegistDateCourseRequestDto> locationList;

    @Builder
    public RegistDateCourseRequestDtoList(List<RegistDateCourseRequestDto> locationList) {

      this.locationList = locationList;
    }
  }

  @Getter
  @Setter
  @NoArgsConstructor
  public static class RegistDateCourseRequestDto {

    private MultipartFile file;
    private String placeName;
    private Float posX;
    private Float posY;
    private String text;
    private List<String> hashTag;

    @Builder
    public RegistDateCourseRequestDto(MultipartFile file, String placeName, Float posX, Float posY,
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
