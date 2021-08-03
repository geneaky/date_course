package me.toy.server.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "likes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class UserDateCourseLike {

  @Id
  @GeneratedValue
  @Column(name = "like_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "datecourse_id")
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
