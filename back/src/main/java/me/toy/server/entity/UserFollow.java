package me.toy.server.entity;

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
public class UserFollow {

  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "USER_ID")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "FOLLOW_ID")
  private Follow follow;

  public void setUser(User user) {

    this.user = user;
    user.getUserFollows().add(this);
  }

  public void setFollow(Follow follow) {

    this.follow = follow;
    follow.getUserFollows().add(this);
  }

  public UserFollow(User user, Follow follow) {

    setUser(user);
    setFollow(follow);
  }
}
