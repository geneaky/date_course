package me.toy.server.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.toy.server.dto.course.CourseRequestDto.RegistLocationFormDto;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Location extends BaseEntity {

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
  @JoinColumn(name = "COURSE_ID")
  private Course course;
  @OneToMany(mappedBy = "location", orphanRemoval = true)
  private List<LocationTag> locationTags = new ArrayList<>();

  public Location(RegistLocationFormDto requestDto, String photoUrl) {

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

  public void setCourse(Course course) {

    this.course = course;
    course.getLocations().add(this);
  }
}
