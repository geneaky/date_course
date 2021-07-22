package me.toy.server.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

  @Id
  @GeneratedValue
  @Column(name = "comment_id")
  private Long id;
  private String commentContents;
  private long thumbUp;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "datecourse_id")
  private DateCourse dateCourse;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  public Comment(User user, DateCourse dateCourse, String commentContents) {
    setUser(user);
    setDateCourse(dateCourse);
    this.commentContents = commentContents;
  }

  public void setUser(User user) {
    this.user = user;
    user.getComments().add(this);
  }

  public void setDateCourse(DateCourse dateCourse) {
    this.dateCourse = dateCourse;
    dateCourse.getComments().add(this);
  }
}
