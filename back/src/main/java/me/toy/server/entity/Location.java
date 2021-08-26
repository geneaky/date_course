package me.toy.server.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import me.toy.server.dto.DateCourseRequestDto.RegistDateCourseRequestDto;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Location {

  @Id
  @GeneratedValue
  @Column(name = "LOCATION_ID")
  private Long id;
  private String name;
  private String text;
  private String photoUrl;
  private Float posx;
  private Float posy;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "DATECOURSE_ID")
  private DateCourse dateCourse;
  @OneToMany(mappedBy = "location", orphanRemoval = true)
  private List<LocationTag> locationTags = new ArrayList<>();

  public Location(RegistDateCourseRequestDto requestDto, String photoUrl) {

    this.name = requestDto.getPlaceName();
    this.text = requestDto.getText();
    this.posx = requestDto.getPosX();
    this.posy = requestDto.getPosY();
    this.photoUrl = photoUrl;
  }

  public Location(String name, String text, String photoUrl, float posx, float posy) {

    this.name = name;
    this.text = text;
    this.photoUrl = photoUrl;
    this.posx = posx;
    this.posy = posy;
  }

  public void setDateCourse(DateCourse dateCourse) {

    this.dateCourse = dateCourse;
    dateCourse.getLocations().add(this);
  }
}
