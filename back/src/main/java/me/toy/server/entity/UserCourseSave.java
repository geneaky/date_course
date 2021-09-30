package me.toy.server.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"USER_ID", "COURSE_ID"})
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserCourseSave {

  @Id
  @GeneratedValue
  @Column(name = "USER_COURSE_SAVE_ID")
  private Long id;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "USER_ID")
  private User user;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "COURSE_ID")
  private Course course;

  public UserCourseSave(User user, Course course) {

    setUser(user);
    setCourse(course);
  }

  public void setUser(User user) {

    this.user = user;
    user.getUserCourseSaves().add(this);
  }

  public void setCourse(Course course) {

    this.course = course;
    course.getUserCourseSaves().add(this);
  }
}
