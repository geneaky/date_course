package me.toy.server.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Course extends BaseEntity {

  @Id
  @GeneratedValue
  @Column(name = "COURSE_ID")
  private Long id;
  private String courseTitle;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "USER_ID")
  private User user;
  @OneToMany(mappedBy = "course", orphanRemoval = true)
  private List<Location> locations = new ArrayList<>();
  @OneToMany(mappedBy = "course", orphanRemoval = true)
  private List<Comment> comments = new ArrayList<>();
  @OneToMany(mappedBy = "course", orphanRemoval = true)
  private List<UserCourseSave> userCourseSaves = new ArrayList<>();
  @OneToMany(mappedBy = "course", orphanRemoval = true)
  private List<UserCourseLike> userCourseLikes = new ArrayList<>();

  public Course(User user, String courseTitle) {

    setUser(user);
    this.courseTitle = courseTitle;
  }

  public void setUser(User user) {

    this.user = user;
    user.getCourse().add(this);
  }

  public int getLikesCount() {

    return this.userCourseLikes.size();
  }
}
