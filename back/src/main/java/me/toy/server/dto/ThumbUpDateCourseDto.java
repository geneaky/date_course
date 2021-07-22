package me.toy.server.dto;

import lombok.Data;
import me.toy.server.entity.DateCourse;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ThumbUpDateCourseDto {

  private Long id;
  private Long thumbUp;
  private List<ThumbUpLocationDto> locations;

  public ThumbUpDateCourseDto(DateCourse dateCourse) {
    this.id = dateCourse.getId();
    this.thumbUp = dateCourse.getThumbUp();
    this.locations = dateCourse.getLocations()
        .stream()
        .map(location -> new ThumbUpLocationDto(location))
        .collect(Collectors.toList());
  }
}
