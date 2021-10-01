package me.toy.server.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
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
    @UniqueConstraint(columnNames = {"USER_ID", "FOLLOWEE_ID"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow {

  private final LocalDateTime created = LocalDateTime.now();

  @Id
  @GeneratedValue
  private Long id;
  @Column(name = "FOLLOWEE_ID")
  private Long followeeId;

  @ManyToOne
  @JoinColumn(name = "USER_ID")
  private User user;

  public Follow(User user, Long followeeId) {
    this.user = user;
    this.followeeId = followeeId;
    user.getFollows().add(this);
  }
}
