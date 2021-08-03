package me.toy.server.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserDateCourseSave {

  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "datecourse_id")
  private DateCourse dateCourse;

  public UserDateCourseSave(User user, DateCourse dateCourse) {
    setUser(user);
    setDateCourse(dateCourse);
  }

  public void setUser(User user) {
    this.user = user;
    user.getUserDateCoursSaves().add(this);
  }

  public void setDateCourse(DateCourse dateCourse) {
    this.dateCourse = dateCourse;
    dateCourse.getUserDateCoursSaves().add(this);
  }
}
