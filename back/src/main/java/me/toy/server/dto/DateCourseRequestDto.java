package me.toy.server.dto;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

public class DateCourseRequestDto {

  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class RegistDateCourseRequestDtoList {

    private ArrayList<@Valid RegistDateCourseRequestDto> locationList;

    @Builder
    public RegistDateCourseRequestDtoList(
        ArrayList<RegistDateCourseRequestDto> locationList) {
      this.locationList = locationList;
    }
  }

  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class RegistDateCourseRequestDto {

    private MultipartFile file;
    @NotNull
    private String placeName;
    @NotNull
    private Float posX;
    @NotNull
    private Float posY;
    private String text;
    private List<String> hashTag;

    @Builder
    public RegistDateCourseRequestDto(MultipartFile file, String placeName, float posX, float posY,
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
