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

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

  @Id
  @GeneratedValue
  @Column(name = "COMMENT_ID")
  private Long id;
  private String content;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "COURSE_ID")
  private Course course;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "USER_ID")
  private User user;
  private Long likes;//COMMENT_LIKE 를 만들어서 연결해야 함

  public Comment(User user, Course course, String content) {

    setUser(user);
    setCourse(course);
    this.content = content;
  }

  public void setUser(User user) {

    this.user = user;
    user.getComments().add(this);
  }

  public void setCourse(Course course) {

    this.course = course;
    course.getComments().add(this);
  }
}
