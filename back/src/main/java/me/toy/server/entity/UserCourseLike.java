package me.toy.server.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class UserCourseLike {

  @Id
  @GeneratedValue
  @Column(name = "USER_COURSE_LIKE_ID")
  private Long id;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "USER_ID")
  private User user;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "COURSE_ID")
  private Course course;

  public UserCourseLike(User user, Course course) {

    setUser(user);
    setCourse(course);
  }

  public void setUser(User user) {

    this.user = user;
    user.getUserCourseLikes().add(this);
  }

  public void setCourse(Course course) {

    this.course = course;
    course.getUserCourseLikes().add(this);
  }

}
