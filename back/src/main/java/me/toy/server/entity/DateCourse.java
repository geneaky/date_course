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
public class DateCourse {

  @Id
  @GeneratedValue
  @Column(name = "datecourse_id")
  private Long id;
  private String dateCourseTitle;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @OneToMany
  private List<UserDateCourseLike> userDateCourseLikes = new ArrayList<>();

  @OneToMany
  private List<Location> locations = new ArrayList<>();

  @OneToMany
  private List<Comment> comments = new ArrayList<>();

  @OneToMany
  private List<UserDateCourseSave> userDateCoursSaves = new ArrayList<>();

  public DateCourse(User user, String dateCourseTitle) {
    setUser(user);
    this.dateCourseTitle = dateCourseTitle;
  }

  public void setUser(User user) {
    this.user = user;
    user.getDateCourses().add(this);
  }
}
