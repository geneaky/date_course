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
  @Column(name = "COMMENT_ID")
  private Long id;
  private String content;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "DATECOURSE_ID")
  private DateCourse dateCourse;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "USER_ID")
  private User user;
  private Long likes;//COMMENT_LIKE 를 만들어서 연결해야 함

  public Comment(User user, DateCourse dateCourse, String content) {

    setUser(user);
    setDateCourse(dateCourse);
    this.content = content;
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
