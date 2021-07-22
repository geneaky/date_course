package me.toy.server.dto;

import lombok.Data;
import me.toy.server.entity.DateCourse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CurrentLocationDateCourseDto {

  private Long id;

  private Long thumbUp;

  private List<CurrentLocationDto> locations;

  public CurrentLocationDateCourseDto(DateCourse dateCourse) {
    this.id = dateCourse.getId();
    this.thumbUp = dateCourse.getThumbUp();
    this.locations = dateCourse.getLocations()
        .stream()
        .map(location -> new CurrentLocationDto(location))
        .collect(Collectors.toList());
  }
}
