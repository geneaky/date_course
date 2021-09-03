package me.toy.server.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow {

  @Id
  @GeneratedValue
  @Column(name = "FOLLOW_ID")
  private Long id;
  private Long followUserId;
  @OneToMany(mappedBy = "follow", orphanRemoval = true)
  private List<UserFollow> userFollows = new ArrayList<>();

  public Follow(Long followUserId) {
    this.followUserId = followUserId;
  }
}
