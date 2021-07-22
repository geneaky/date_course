package me.toy.server.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SavedCourse {

  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "datecourse_id")
  private DateCourse dateCourse;

  public SavedCourse(User user, DateCourse dateCourse) {
    setUser(user);
    setDateCourse(dateCourse);
  }

  public void setUser(User user) {
    this.user = user;
    user.getSavedCourses().add(this);
  }

  public void setDateCourse(DateCourse dateCourse) {
    this.dateCourse = dateCourse;
    dateCourse.getSavedCourses().add(this);
  }
}
