package me.toy.server.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DateCourse extends BaseEntity {

  @Id
  @GeneratedValue
  @Column(name = "DATECOURSE_ID")
  private Long id;
  private String dateCourseTitle;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "USER_ID")
  private User user;
  @OneToMany(mappedBy = "dateCourse", orphanRemoval = true)
  private List<Location> locations = new ArrayList<>();
  @OneToMany(mappedBy = "dateCourse", orphanRemoval = true)
  private List<Comment> comments = new ArrayList<>();
  @OneToMany(mappedBy = "dateCourse", orphanRemoval = true)
  private List<UserDateCourseSave> userDateCoursSaves = new ArrayList<>();
  @OneToMany(mappedBy = "dateCourse", orphanRemoval = true)
  private List<UserDateCourseLike> userDateCourseLikes = new ArrayList<>();

  public DateCourse(User user, String dateCourseTitle) {

    setUser(user);
    this.dateCourseTitle = dateCourseTitle;
  }

  public void setUser(User user) {

    this.user = user;
    user.getDateCourses().add(this);
  }
}
