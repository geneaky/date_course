package me.toy.server.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import lombok.Setter;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class UserDateCourseLike {

  @Id
  @GeneratedValue
  @Column(name = "USERDATECOURSELIKE_ID")
  private Long id;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "USER_ID")
  private User user;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "DATECOURSE_ID")
  private DateCourse dateCourse;

  public UserDateCourseLike(User user, DateCourse dateCourse) {

    setUser(user);
    setDateCourse(dateCourse);
  }

  public void setUser(User user) {

    this.user = user;
    user.getUserDateCourseLikes().add(this);
  }

  public void setDateCourse(DateCourse dateCourse) {

    this.dateCourse = dateCourse;
    dateCourse.getUserDateCourseLikes().add(this);
  }

}
